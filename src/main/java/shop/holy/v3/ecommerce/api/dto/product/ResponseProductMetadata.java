package shop.holy.v3.ecommerce.api.dto.product;

import java.math.BigDecimal;
import java.util.List;

public record ResponseProductMetadata(
        long id,
        String slug,
        String name,
        String imageUrl,
        BigDecimal price,
        BigDecimal originalPrice
) {
}
