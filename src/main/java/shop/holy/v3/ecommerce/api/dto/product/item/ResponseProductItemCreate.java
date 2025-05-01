package shop.holy.v3.ecommerce.api.dto.product.item;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResponseProductItemCreate(
        @Schema(description = "List of product keys that successfully added",
                example = "[\"SPOTIFYKEY123\", \"SPOTIFYKEY124\"]")
        String[] accepted
) {
}