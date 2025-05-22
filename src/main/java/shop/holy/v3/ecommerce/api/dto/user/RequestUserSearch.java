package shop.holy.v3.ecommerce.api.dto.user;

import shop.holy.v3.ecommerce.api.dto.RequestPageable;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestUserSearch(
        @Schema(description = "Pagination and sorting information")
        RequestPageable pageRequest,
        
        @Schema(description = "Filter by user IDs", example = "[1, 2, 3]")
        List<Long> ids,

        @Schema(description = "Filter by full name", example = "John Doe")
        String fullName,
        
        @Schema(description = "Include deleted users", example = "false", defaultValue = "false")
        boolean deleted,

        @Schema(description = "Filter by product IDs", example = "[\"ADMIN\", \"STAFF\"]")
        List<String> roles,
        
        @Schema(description = "Filter by creation date (from)", format = "date", example = "2023-01-01")
        LocalDate createdAtFrom,
        
        @Schema(description = "Filter by creation date (to)", format = "date", example = "2025-12-31")
        LocalDate createdAtTo,

        @Schema(description = "Filter by email address", example = "user@example.com")
        String email
) {
}
