package shop.holy.v3.ecommerce.api.dto.product.item;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestProductItemCreate(
        long productId,
        @Nonnull @NotBlank @Size(max = 30) @Pattern(
                regexp = "^[a-zA-Z0-9]+$",
                message = "Field must contain only letters and numbers"
        )
        String productKey,
        @Nonnull @NotBlank @Size(max = 30) @Pattern(
                regexp = "^[a-zA-Z0-9]+$",
                message = "Field must contain only letters and numbers"
        )
        String region
) {
}
