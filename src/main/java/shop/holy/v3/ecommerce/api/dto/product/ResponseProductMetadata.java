package shop.holy.v3.ecommerce.api.dto.product;

import java.math.BigDecimal;
import java.util.List;

public record ResponseProductMetadata(
        String slug,
        String name,
        String imageUrl,
        BigDecimal price,
        Double discountPercent,
        List<String> categoryIds
) {
}
