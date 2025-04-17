package shop.holy.v3.ecommerce.api.dto.blog;

import org.springframework.data.domain.Pageable;
import shop.holy.v3.ecommerce.api.dto.RequestPageable;

import java.time.LocalDate;
import java.util.List;

public record RequestBlogSearch(
        RequestPageable pageRequest,
        String search,
        List<String> tags,
        LocalDate publishedFrom,
        LocalDate publishedTo,
        boolean deleted) {
}
