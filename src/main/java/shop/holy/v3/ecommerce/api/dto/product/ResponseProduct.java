package shop.holy.v3.ecommerce.api.dto.product;

import shop.holy.v3.ecommerce.api.dto.category.ResponseCategory;
import shop.holy.v3.ecommerce.shared.constant.ProductStatus;

import java.math.BigDecimal;
import java.util.Date;

public record ResponseProduct(
        long id,
        ResponseDescription productDescription,
        String slug,
        String name,
        String imageUrl,
        BigDecimal price,
        BigDecimal originalPrice,
        ResponseCategory[] categories,
        ProductStatus status,
        ResponseProductMetadata[] variants
) {
}
