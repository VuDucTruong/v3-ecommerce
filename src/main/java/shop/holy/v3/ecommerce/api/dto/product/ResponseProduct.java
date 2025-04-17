package shop.holy.v3.ecommerce.api.dto.product;

import shop.holy.v3.ecommerce.api.dto.category.ResponseCategory;

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
        Date availableFrom,
        Date availableTo,
        ResponseCategory[] categories,
        ResponseProductMetadata[] variants
) {
}
