package shop.holy.v3.ecommerce.api.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestCommentUpdate(
        long id,
        @NotNull(message = "comment must not Empty") @NotBlank String content) {
}
