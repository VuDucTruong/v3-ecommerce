package shop.holy.v3.ecommerce.api.dto.product.item;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestProductItemCreate(
        @Schema(example = "1234") long productId,
        @Schema(description = "Unique alphanumeric identifier for the product product",
                example = "SPOTIFYKEY123", 
                pattern = "^[a-zA-Z0-9]+$",
                maxLength = 30) 
        String productKey,
        @Schema(description = "Region code for the product product",
                example = "US", 
                pattern = "^[a-zA-Z0-9]+$",
                maxLength = 30) 
        String region
) {
}