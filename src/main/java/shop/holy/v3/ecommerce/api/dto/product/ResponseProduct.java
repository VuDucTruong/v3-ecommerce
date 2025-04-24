package shop.holy.v3.ecommerce.api.dto.product;

import shop.holy.v3.ecommerce.api.dto.category.ResponseCategory;
import shop.holy.v3.ecommerce.api.dto.product.description.ResponseDescription;
import shop.holy.v3.ecommerce.shared.constant.ProductStatus;

import java.math.BigDecimal;

public record ResponseProduct(
        long id,
        ResponseDescription productDescription,
        String slug,
        String name,
        String imageUrl,
        String[] tags,
        boolean isRepresent,
        BigDecimal price,
        BigDecimal originalPrice,
        ResponseCategory[] categories,
        ProductStatus status,
        ResponseProductMetadata[] variants
) {
}
