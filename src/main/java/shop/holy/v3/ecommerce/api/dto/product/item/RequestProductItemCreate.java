package shop.holy.v3.ecommerce.api.dto.product.item;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;
import org.hibernate.validator.constraints.Length;

public record RequestProductItemCreate(
        @Schema(example = "1234")
        @Nonnull Long productId,
        @Schema(description = "Unique alphanumeric identifier for the product product",
                example = "SPOTIFYKEY123",
                pattern = "^[a-zA-Z0-9]+$",
                maxLength = 30)
        @Nonnull @Length(min = 1, max = 40) String productKey,
        @Schema(description = "Region code for the product product",
                example = "US",
                pattern = "^[a-zA-Z0-9]+$",
                maxLength = 30)
        String region
) {
}