package shop.holy.v3.ecommerce.api.dto.comment;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestCommentSearch(
        @Schema(description = "Filter by comment IDs", example = "[1, 2, 3]")
        Set<Long> ids,
        
        @Schema(description = "Include deleted comments", example = "false", defaultValue = "false")
        boolean deleted
) {
}
