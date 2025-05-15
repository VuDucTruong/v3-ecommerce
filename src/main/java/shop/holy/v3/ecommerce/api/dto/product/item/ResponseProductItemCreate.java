package shop.holy.v3.ecommerce.api.dto.product.item;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ResponseProductItemCreate(
        @Schema(description = "List of product keys that successfully added",
                example = "[\"SPOTIFYKEY123\", \"SPOTIFYKEY124\"]")
        List<ResponseAccepted> accepted
) {
    public record ResponseAccepted(
            Long productId,
            String productKey
    ) {
    }
}