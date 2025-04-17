package shop.holy.v3.ecommerce.api.dto.product;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
        List<String> categoryIds,
        List<ResponseProductMetadata> variants
) {
}
