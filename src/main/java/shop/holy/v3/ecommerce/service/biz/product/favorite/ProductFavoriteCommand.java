package shop.holy.v3.ecommerce.service.biz.product.favorite;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.persistence.entity.product.Product;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductFavoriteRepository;
import shop.holy.v3.ecommerce.shared.mapper.product.ProductMapper;
import shop.holy.v3.ecommerce.shared.mapper.product.ProductTagMapper;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFavoriteCommand {
    private final IProductFavoriteRepository favoriteRepository;
    private final ProductMapper productMapper;
    private final ProductTagMapper tagMapper;

    @Transactional
    public void addFavorite(Long productId) {
        long accountId = SecurityUtil.getAuthNonNull().getProfileId();
        favoriteRepository.insertFavoriteProduct(accountId, productId);
    }

    @Transactional
    public void removeFavorite(Long productId) {
        long accountId = SecurityUtil.getAuthNonNull().getProfileId();
        favoriteRepository.deleteFavoriteProduct(accountId, productId);
    }

//    @Transactional
    public ResponsePagination<ResponseProduct> findFavorites(Pageable pageable) {
        long accountId = SecurityUtil.getAuthNonNull().getProfileId();
        Page<Product> products = favoriteRepository.findFavorites(accountId, pageable);
        Page<ResponseProduct> responses = products.map(p->productMapper.fromEntityToResponse_Light(p, tagMapper.fromTagEntitiesToStringTags(p.getTags()) ,true));
        return ResponsePagination.fromPage(responses);
    }

    public List<Long> getFavoriteProductIds() {
        long accountId = SecurityUtil.getAuthNonNull().getProfileId();
        List<Long> favoriteIds = favoriteRepository.findAllIdsByAccountId(accountId);
        if (CollectionUtils.isEmpty(favoriteIds)) {
            return Collections.emptyList();
        }
        return favoriteIds;
    }

}
