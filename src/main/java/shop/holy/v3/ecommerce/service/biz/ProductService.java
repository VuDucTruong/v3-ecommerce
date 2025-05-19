package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductCreate;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductSearch;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductUpdate;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.persistence.entity.EntityBase;
import shop.holy.v3.ecommerce.persistence.entity.Product;
import shop.holy.v3.ecommerce.persistence.entity.ProductDescription;
import shop.holy.v3.ecommerce.persistence.entity.ProductItem;
import shop.holy.v3.ecommerce.persistence.repository.IProductDescriptionRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProductFavoriteRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProductItemRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProductRepository;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryFacadeService;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.DefaultValues;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;
import shop.holy.v3.ecommerce.shared.mapper.ProductMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductMapper productMapper;
    private final IProductRepository productRepository;
    private final IProductDescriptionRepository productDescriptionRepository;
    private final IProductItemRepository productItemRepository;
    private final IProductFavoriteRepository favoriteRepository;
    private final CloudinaryFacadeService cloudinaryFacadeService;

    @SneakyThrows
    public ResponsePagination<ResponseProduct> search(RequestProductSearch searchReq) {
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(searchReq.pageRequest());
        Specification<Product> spec = productMapper.fromRequestSearchToSpec(searchReq);
        Page<Product> products = productRepository.findAll(spec, pageable);
        AuthAccount authAccount = SecurityUtil.getAuthNullable();
        Page<ResponseProduct> resultsPage;
        if (authAccount != null && authAccount.getProfileId() != null) {
            Set<Long> productIds = products.getContent().stream().map(Product::getId).collect(Collectors.toSet());
            Set<Long> favoriteProductIds = favoriteRepository.checkFavorites(authAccount.getProfileId(), productIds);
            resultsPage = products.map(p -> productMapper.fromEntityToResponse_Light(p, favoriteProductIds.contains(p.getId())));
        } else
            resultsPage = products.map(p -> productMapper.fromEntityToResponse_Light(p, false));

        return ResponsePagination.fromPage(resultsPage);
    }

    public CompletableFuture<ResponseProduct> getByIdentifier(Long id, String slug, boolean deleted) {
        if (id == null && slug == null)
            return CompletableFuture.completedFuture(null);

        CompletableFuture<Product> prod = CompletableFuture.supplyAsync(() -> {
                    if (id != null) {
                        if (deleted)
                            return productRepository.findByIdWithJoinFetch(id);
                        else
                            return productRepository.findFirstByIdEqualsAndDeletedAtIsNull(id);
                    } else {
                        return deleted
                                ? productRepository.findFirstBySlug(slug)
                                : productRepository.findFirstBySlugEqualsAndDeletedAtIsNull(slug);
                    }
                })
                .thenApply(opt -> opt.orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception));

        AuthAccount authAccount = SecurityUtil.getAuthNullable();
        CompletableFuture<Boolean> isFav;
        if (authAccount != null) {
            Long profileId = authAccount.getProfileId();
            isFav = profileId != null
                    ? CompletableFuture.supplyAsync(() -> favoriteRepository.checkFavorite(profileId, id).isPresent())
                    : CompletableFuture.completedFuture(false);
        } else isFav = CompletableFuture.completedFuture(false);

        /// NO ADMIN HANDLING ==> no productItems revealed
        if (authAccount == null || !authAccount.isAdmin()) /// already null check fisrt then -> check NOT ADMIN
            return CompletableFuture.allOf(prod, isFav).thenApply(v -> productMapper.fromEntityToResponse_Light(prod.join(), isFav.join()));

        ///  WHEN ADMIN, CAN SEE first 10 items orderedBy created at
        Pageable pageableItems = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        CompletableFuture<Slice<ProductItem>> itemsSlice =
                CompletableFuture.supplyAsync(() ->
                {
                    if (id != null)
                        return productItemRepository.findAllByProductIdEquals(id, pageableItems);
                    else
                        return productItemRepository.findAllByProductSlug(slug, pageableItems);
                });

        return CompletableFuture.allOf(prod, itemsSlice, isFav)
                .thenApply(v -> {
                    List<ProductItem> items = itemsSlice.join().getContent();
                    Product p = prod.join();
                    return productMapper.fromEntity_Items_ToResponse_Detailed(p, items, isFav.join());
                });
    }

    @Transactional
    public ResponseProduct insert(RequestProductCreate request) {
        Product product = productMapper.fromCreateRequestToEntity(request);

        ///  UPLOAD images
        this.uploadSingleImageAndSetEntity(product, request.image());
        ProductDescription prod_Desc = productMapper.fromRequestToDescription(request.productDescription());
        var desc = productDescriptionRepository.save(prod_Desc);

        /// SAVE PRODUCT_DESC
        product.setProDescId(desc.getId());
        Product rs = productRepository.save(product);

        ///  SAVE CATEGORIES
        {
            request.categoryIds().forEach(catId -> {
                productRepository.insertProductCategories(product.getId(), catId);
            });
        }
        return productMapper.fromEntityToResponse_Light(rs, false);
    }

    @Transactional
    public ResponseProduct update(RequestProductUpdate request) throws IOException {
        Product product = productMapper.fromRequestUpdateToEntity(request);
        long productId = request.id();

        this.uploadSingleImageAndSetEntity(product, request.image());
        ///  append-delete Categories
        if (!CollectionUtils.isEmpty(request.categoryIds())) {
            List<Long> commingIds = request.categoryIds();
            Set<Long> currentCatIds = productRepository.findCategoryIdsByProductId(productId);
            List<Long> toDeletes = currentCatIds.stream()
                    .filter(id -> !commingIds.contains(id))
                    .toList();
            List<Long> toInserts = commingIds.stream()
                    .filter(id -> !currentCatIds.contains(id))
                    .toList();
            toDeletes.forEach(catId -> {
                productRepository.deleteProductCategories(productId, catId);
            });
            toInserts.forEach(catId -> {
                productRepository.insertProductCategories(productId, catId);
            });
        }

        ///  update product && update description as well
        {
            Product rs = productRepository.save(product);
            ProductDescription prod_Desc = productMapper.fromRequestToDescription(request.productDescription());
            if (prod_Desc != null) {
//                prod_Desc.setProductId(rs.getId());
                prod_Desc.setId(rs.getProDescId());
                productDescriptionRepository.updateProductDescriptionById(prod_Desc);
            }
        }

        /// TODO: use Redis cache -> get Profile -> favoriteProductIds -> if match -> true
        return productMapper.fromEntityToResponse_Light(product, false);
    }

    @Transactional
    public int deleteProductById(Long id) {
        if (id == null)
            return 0;
        return productRepository.updateProductDeletedAt(id);
    }

    @Transactional
    public int deleteProductByIdIn(long[] ids) {
        if (ids == null || ids.length == 0)
            return 0;
        return productRepository.updateProductDeletedAtByIdIn(ids);
    }


    private void uploadSingleImageAndSetEntity(Product product, MultipartFile file) {
        if (file == null)
            return;
        String imageUrl = cloudinaryFacadeService.uploadProductBlob(file);
        product.setImageUrlId(imageUrl);
    }


}
