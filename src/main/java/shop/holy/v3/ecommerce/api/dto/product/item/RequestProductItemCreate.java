package shop.holy.v3.ecommerce.api.dto.product.item;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestProductItemCreate(
        @Schema(example = "1234") long productId,
        @Schema(description = "Unique alphanumeric identifier for the product item", 
                example = "SPOTIFYKEY123", 
                pattern = "^[a-zA-Z0-9]+$",
                maxLength = 30) 
        String productKey,
        @Schema(description = "Region code for the product item", 
                example = "US", 
                pattern = "^[a-zA-Z0-9]+$",
                maxLength = 30) 
        String region,
        @Schema(description = "not present -> false: false by default") boolean used
) {
}