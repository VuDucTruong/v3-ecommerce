package shop.holy.v3.ecommerce.api.dto.comment;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.PageRequest;
import shop.holy.v3.ecommerce.api.dto.RequestPageable;

public record RequestCommentSearch(
        RequestPageable pageRequest,
        @Schema(description = "Filter by comment IDs", example = "[1, 2, 3]")
        Set<Long> ids,
        @Schema(description = "will returns all the product's schemas") Long productId,
        
        @Schema(description = "Include deleted comments", example = "false", defaultValue = "false")
        boolean deleted
) {
}
