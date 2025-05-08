package shop.holy.v3.ecommerce.api.dto.product.item;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResponseProductItem(
        @Schema(example = "1234") long productId,
        @Schema(example = "SPOTIFYKEY123") String productKey,
        @Schema(example = "US") String region,
        @Schema(description = "Whether the product product is available for purchase") boolean active
) {
}