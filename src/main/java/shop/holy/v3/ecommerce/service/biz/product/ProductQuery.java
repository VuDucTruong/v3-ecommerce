package shop.holy.v3.ecommerce.service.biz.product;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductSearch;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.persistence.entity.product.Product;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductGroup;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductItem;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductFavoriteRepository;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductItemRepository;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.mapper.product.ProductMapper;
import shop.holy.v3.ecommerce.shared.mapper.product.ProductTagMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductQuery {

    private final ProductMapper productMapper;
    private final ProductTagMapper productTagMapper;

    private final IProductRepository productRepository;
    private final IProductItemRepository itemRepository;
    private final IProductFavoriteRepository favoriteRepository;

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
            resultsPage = products.map(p -> productMapper.fromEntityToResponse_Light(p, productTagMapper.fromTagEntitiesToStringTags(p.getTags()), favoriteProductIds.contains(p.getId())));
        } else
            resultsPage = products.map(p -> productMapper.fromEntityToResponse_Light(p, productTagMapper.fromTagEntitiesToStringTags(p.getTags()), false));

        return ResponsePagination.fromPage(resultsPage);
    }

    public CompletableFuture<ResponseProduct> getByIdentifier(Long productId, String slug, boolean deleted) {
        if (productId == null && !StringUtils.hasLength(slug))
            return CompletableFuture.completedFuture(null);

        CompletableFuture<Product> prod = CompletableFuture.supplyAsync(() -> {
                    if (productId != null) {
                        if (deleted)
                            return productRepository.findByIdWithJoinFetch(productId);
                        else
                            return productRepository.findFirstByIdEqualsAndDeletedAtIsNull(productId);
                    } else {
                        return deleted
                                ? productRepository.findFirstBySlug(slug)
                                : productRepository.findFirstBySlugEqualsAndDeletedAtIsNull(slug);
                    }
                })
                .thenApply(opt -> opt.orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception))
                .thenApply(p -> {
                    Set<Product> variants;
                    if (p.getGroupId() == null || p.getGroupId() == 1)
                        variants = Set.of(p);
                    else if (deleted)
                        variants = productRepository.findByGroupId(p.getGroupId());
                    else
                        variants = productRepository.findByGroupIdAndDeletedAtIsNull(p.getGroupId());
                    ProductGroup group = new ProductGroup();
                    group.setId(p.getGroupId());
                    group.setVariants(variants);
                    p.setGroup(group);
                    return p;
                });

        AuthAccount authAccount = SecurityUtil.getAuthNullable();
        CompletableFuture<Boolean> isFav;
        if (authAccount != null && authAccount.getProfileId() != null) {
            long profileId = authAccount.getProfileId();
            if (productId != null)
                isFav = CompletableFuture.supplyAsync(() -> favoriteRepository.checkFavorite(profileId, productId).isPresent());
            else
                isFav = prod.thenApply(p -> favoriteRepository.checkFavorite(profileId, p.getId()).isPresent());
        } else isFav = CompletableFuture.completedFuture(false);

        /// NO ADMIN HANDLING ==> no productItems revealed
        if (authAccount == null || !authAccount.isAdmin()) /// already null check fisrt then -> check NOT ADMIN
            return CompletableFuture.allOf(prod, isFav).thenApply(v -> {
                var p = prod.join();
                return productMapper.fromEntityToResponse_Light(p, productTagMapper.fromTagEntitiesToStringTags(p.getTags()), isFav.join());
            });

        ///  WHEN ADMIN, CAN SEE first 10 items orderedBy created at
        Pageable pageableItems = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        CompletableFuture<Slice<ProductItem>> itemsSlice =
                CompletableFuture.supplyAsync(() ->
                {
                    if (productId != null)
                        return itemRepository.findAllByProductIdEquals(productId, pageableItems);
                    else
                        return itemRepository.findAllByProductSlug(slug, pageableItems);
                });

        return CompletableFuture.allOf(prod, itemsSlice, isFav)
                .thenApply(v -> {
                    List<ProductItem> items = itemsSlice.join().getContent();
                    Product p = prod.join();
                    return productMapper.fromEntity_Items_ToResponse_Detailed(p, productTagMapper.fromTagEntitiesToStringTags(p.getTags()), items, isFav.join());
                });
    }

}
