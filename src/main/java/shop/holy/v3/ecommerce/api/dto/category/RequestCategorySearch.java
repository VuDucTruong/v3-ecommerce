package shop.holy.v3.ecommerce.api.dto.category;

import shop.holy.v3.ecommerce.api.dto.RequestPageable;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestCategorySearch(
        @Schema(description = "Pagination and sorting information")
        RequestPageable pageRequest,

        long[] ids,

        @Schema(description = "Search by category name", example = "electronics")
        String search,

        @Schema(description = "Include deleted categories", example = "false", defaultValue = "false")
        boolean deleted
) {
}
