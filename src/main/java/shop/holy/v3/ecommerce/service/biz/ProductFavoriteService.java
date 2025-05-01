package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.persistence.entity.Product;
import shop.holy.v3.ecommerce.persistence.repository.IProductRepository;
import shop.holy.v3.ecommerce.shared.mapper.ProductMapper;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

@Service
@RequiredArgsConstructor
public class ProductFavoriteService {
    private final IProductRepository productRepository;
    private final ProductMapper productMapper;

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
        Page<ResponseProduct> responses = products.map(productMapper::fromEntityToResponse_Light);
        return ResponsePagination.fromPage(responses);
    }
}
