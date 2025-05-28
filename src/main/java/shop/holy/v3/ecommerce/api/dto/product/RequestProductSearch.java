package shop.holy.v3.ecommerce.api.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import shop.holy.v3.ecommerce.api.dto.RequestPageable;

import java.math.BigDecimal;
import java.util.List;

public record RequestProductSearch(
        @Schema(description = "Pagination and sorting information")
        RequestPageable pageRequest,

        @Schema(description = "Filter by product IDs", example = "[1, 2, 3]")
        List<Long> ids,

        @Schema(description = "Filter by category IDs", example = "[1, 2, 3]")
        List<Long> categoryIds,

        @Schema(description = "Search by product name or description", example = "spotify")
        String search,

        @Schema(description = "Filter by minimum price", example = "100.00")
        BigDecimal priceFrom,

        @Schema(description = "Filter by maximum price", example = "1000.00")
        BigDecimal priceTo,

        List<String> tags,

        @Schema(description = "Include deleted products", example = "false", defaultValue = "false")
        boolean deleted
) {
}
