package shop.holy.v3.ecommerce.api.dto.product.item;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Date;

public record ResponseProductItems_Indetails(
        long id,
        String slug,
        String name,
        String imageUrl,
        boolean represent,
        BigDecimal price,
        BigDecimal originalPrice,
        @Schema(example = "1234") long productId,
        @Schema(example = "SPOTIFYKEY123") String productKey,
        Date createdAt,
        @Schema(example = "US") String region,
        @Schema(description = "Whether the product product is available for purchase") boolean used

) {

}
