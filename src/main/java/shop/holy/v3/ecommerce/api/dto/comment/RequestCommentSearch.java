package shop.holy.v3.ecommerce.api.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import shop.holy.v3.ecommerce.api.dto.RequestPageable;

import java.util.Set;

public record RequestCommentSearch(
        RequestPageable pageRequest,
        @Schema(description = "Filter by comment IDs", example = "[1, 2, 3]")
        Set<Long> ids,
        @Schema(description = "will returns all the product's schemas") Long productId,
        String search,
        String productName,

        @Schema(description = "Include deleted comments", example = "false", defaultValue = "false")
        boolean deleted
) {
//        @AssertTrue
//        public boolean isValid(){
//                return ids != null && !ids.isEmpty();
//        }
}
