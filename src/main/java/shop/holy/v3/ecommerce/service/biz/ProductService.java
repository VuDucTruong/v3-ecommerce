package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductCreate;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductSearch;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductUpdate;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.persistence.entity.Product;
import shop.holy.v3.ecommerce.persistence.entity.ProductDescription;
import shop.holy.v3.ecommerce.persistence.entity.ProductItem;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProdId_ProdItem;
import shop.holy.v3.ecommerce.persistence.repository.IProductDescriptionRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProductItemRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProductRepository;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryFacadeService;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.DefaultValues;
import shop.holy.v3.ecommerce.shared.constant.ProductStatus;
import shop.holy.v3.ecommerce.shared.mapper.ProductMapper;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductMapper productMapper;
    private final IProductRepository productRepository;
    private final IProductDescriptionRepository productDescriptionRepository;
    private final IProductItemRepository productItemRepository;
    private final CloudinaryFacadeService cloudinaryFacadeService;

    @SneakyThrows
    public ResponsePagination<ResponseProduct> search(RequestProductSearch searchReq) {
        Pageable pageable = productMapper.fromRequestPageableToPageable(searchReq.pageRequest());
        Specification<Product> spec = productMapper.fromRequestSearchToSpec(searchReq);
        Page<Product> products = productRepository.findAll(spec, pageable);

        long[] ids = products.stream()
                .mapToLong(Product::getId)
                .toArray();

        List<ProQ_ProdId_ProdItem> pairs = ids.length > 0 ? productItemRepository.find_count_group_by_prodId(ids) : List.of();

        Page<ResponseProduct> responses;
        if (!CollectionUtils.isEmpty(pairs)) {
            Map<Long, ProQ_ProdId_ProdItem> pairMap = pairs.stream()
                    .collect(Collectors.toMap(ProQ_ProdId_ProdItem::getProductId, Function.identity()));
            responses = products.map(product -> {
                        ProductStatus status = ProductStatus.fromCnt(pairMap.get(product.getId()).getCnt());
                        return productMapper.fromEntityToResponseWithStatus(product, status);
                    }
            );
        } else
            responses = products.map(productMapper::fromEntityToResponse);

        return ResponsePagination.fromPage(responses);
    }

    public ResponseProduct getByIdentifier(long id, String slug, boolean deleted) {
        if (id == DefaultValues.ID && slug == null)
            return null;
        Product rs;
        if (id != DefaultValues.ID) {
            if (deleted)
                rs = productRepository.findByIdWithJoinFetch(id).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
            else
                rs = productRepository.findFirstByIdEqualsAndDeletedAtIsNull(id).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
        } else {
            if (deleted)
                rs = productRepository.findFirstBySlug(slug).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
            else
                rs = productRepository.findFirstBySlugEqualsAndDeletedAtIsNull(slug).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
        }
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<ProductItem> productItems = productItemRepository.findAllByProductIdEquals(id, pageable);
        rs.setProductItems(new HashSet<>(productItems.getContent()));

        return productMapper.fromEntityToResponseWithStatus(rs, productMapper.fromCntToStatus(productItems.getTotalElements()));
    }

    @Transactional
    public ResponseProduct insert(RequestProductCreate request) throws IOException {
        Product product = productMapper.fromCreateRequestToEntity(request);

        ///  UPLOAD images
        this.uploadSingleImageAndSetEntity(product, request.image());

        /// SAVE PRODUCT_DESC
        Product rs = productRepository.save(product);
        {
            ProductDescription prod_Desc = productMapper.fromRequestToDescription(request.productDescription());
            prod_Desc.setProductId(rs.getId());
            productDescriptionRepository.save(prod_Desc);
        }
        ///  SAVE CATEGORIES
        {
            request.categoryIds().forEach(catId -> {
                productRepository.insertProductCategories(product.getId(), catId);
            });
        }
        return productMapper.fromEntityToResponse(product);
    }

    @Transactional
    public ResponseProduct update(RequestProductUpdate request) throws IOException {
        Product product = productMapper.fromRequestUpdateToEntity(request);
        long productId = request.id();

        this.uploadSingleImageAndSetEntity(product, request.image());
        ///  append-delete Categories
        request.catIdsToDelete().forEach(catId -> {
            productRepository.deleteProductCategories(productId, catId);
        });
        request.catIdsToAdd().forEach(catId -> {
            productRepository.insertProductCategories(productId, catId);
        });
        ///  update product && update description as well
        {
            Product rs = productRepository.save(product);
            ProductDescription prod_Desc = productMapper.fromRequestToDescription(request.productDescription());
            if (prod_Desc != null) {
                prod_Desc.setProductId(rs.getId());
                productDescriptionRepository.updateProductDescriptionById(prod_Desc);
            }
        }
        return productMapper.fromEntityToResponse(product);
    }

    @Transactional
    public int deleteProductById(Long id) {
        return productRepository.updateProductDeletedAt(id);
    }

    @Transactional
    public void addFavorite(Long productId) {
        long accountId = SecurityUtil.getAuthNonNull().getProfileId();
        productRepository.insertFavoriteProduct(accountId, productId);
    }

    @Transactional
    public void removeFavorite(Long productId) {
        long accountId = SecurityUtil.getAuthNonNull().getProfileId();
        productRepository.deleteFavoriteProduct(accountId, productId);
    }

    @Transactional
    public ResponsePagination<ResponseProduct> findFavorites(Pageable pageable) {
        long accountId = SecurityUtil.getAuthNonNull().getProfileId();
        Page<Product> products = productRepository.findFavorites(accountId, pageable);
        Page<ResponseProduct> responses = products.map(productMapper::fromEntityToResponse);
        return ResponsePagination.fromPage(responses);
    }

    private void uploadSingleImageAndSetEntity(Product product, MultipartFile file) throws IOException {
        if (file == null)
            return;
        String imageUrl = cloudinaryFacadeService.uploadProductBlob(file);
        product.setImageUrlId(imageUrl);
    }


}
