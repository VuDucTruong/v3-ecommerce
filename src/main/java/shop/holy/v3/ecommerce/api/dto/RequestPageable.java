package shop.holy.v3.ecommerce.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestPageable(
        @Schema(description = "Page number (starting from 0)", example = "0")
        int page,
        @Schema(description = "Number of items per page", example = "10")
        int size,
        @Schema(description = "Sort by field's name", example = "name")
        String sortBy,
        @Schema(description = "Sort direction", example = "asc")
        String sortDirection
) {
}
