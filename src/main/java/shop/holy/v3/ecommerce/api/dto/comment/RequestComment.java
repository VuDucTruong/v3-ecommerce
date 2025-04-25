package shop.holy.v3.ecommerce.api.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestComment(
        @Schema(description = "Product ID", example = "9")
        long productId,
        @Schema(description = "null -> reply, non-null -> simple comment", example = "null")
        Long parentCommentId,
        String content
) {

}
