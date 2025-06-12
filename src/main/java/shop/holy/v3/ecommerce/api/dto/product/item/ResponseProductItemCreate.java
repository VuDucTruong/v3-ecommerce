package shop.holy.v3.ecommerce.api.dto.product.item;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

public record ResponseProductItemCreate(
        @Schema(description = "List of product keys that successfully added",
                example = """
                        [{
                           \"productId\" : \"8\",
                           \"productKey\" : \"SPOTIFYKEY123\"
                        }]
                        """)
        List<ResponseAccepted> productItemDetails
) {
    public record ResponseAccepted(
            Long id,
            Long productId,
            String productKey,
            Map<String, String> account
    ) {
    }
}