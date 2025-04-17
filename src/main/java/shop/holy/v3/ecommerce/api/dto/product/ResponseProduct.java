package shop.holy.v3.ecommerce.api.dto.product;

import shop.holy.v3.ecommerce.persistence.entity.Category;

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
        Category[] categories,
        ResponseProductMetadata[] variants
) {
}
