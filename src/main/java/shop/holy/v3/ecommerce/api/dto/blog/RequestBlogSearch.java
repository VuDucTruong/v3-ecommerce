package shop.holy.v3.ecommerce.api.dto.blog;

import org.springframework.data.domain.Pageable;
import shop.holy.v3.ecommerce.api.dto.RequestPageable;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record RequestBlogSearch(
        @Schema(description = "Pagination and sorting information")
        RequestPageable pageRequest,
        
        @Schema(description = "Search by blog title or author", example = "tech")
        String search,
        
        @Schema(description = "Filter by blog tags", example = "[\"tech\", \"news\", \"reviews\"]")
        List<String> tags,
        
        @Schema(description = "Filter by publication start date", format = "date", example = "2023-01-01")
        LocalDate publishedFrom,
        
        @Schema(description = "Filter by publication end date", format = "date", example = "2023-12-31")
        LocalDate publishedTo,
        
        @Schema(description = "Include deleted blogs", example = "false", defaultValue = "false")
        boolean deleted) {
}
