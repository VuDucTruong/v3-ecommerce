package shop.holy.v3.ecommerce.api.dto.blog;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public record RequestBlogSearch(
        Pageable pageRequest,
        String search,
        List<String> tags,
        LocalDate publishedFrom,
        LocalDate publishedTo,
        boolean deleted) {
}
