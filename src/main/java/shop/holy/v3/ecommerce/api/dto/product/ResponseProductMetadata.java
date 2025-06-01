package shop.holy.v3.ecommerce.api.dto.product;

import java.math.BigDecimal;

public record ResponseProductMetadata(
        long id,
        String slug,
        String name,
        String imageUrl,
        long quantity,

        boolean represent,
        BigDecimal price,
        BigDecimal originalPrice
) {
}
