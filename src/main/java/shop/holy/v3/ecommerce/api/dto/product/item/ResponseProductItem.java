package shop.holy.v3.ecommerce.api.dto.product.item;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.Map;

public record ResponseProductItem(
        long id,
        @Schema(example = "1234") long productId,
        @Schema(example = "SPOTIFYKEY123") String productKey,
        Map<String,String> account,
        Date createdAt,
        @Schema(example = "US") String region,
        @Schema(description = "Whether the product product is available for purchase") boolean used
) {
}