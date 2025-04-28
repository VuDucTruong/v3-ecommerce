package shop.holy.v3.ecommerce.api.dto.blog.genre;

import java.util.List;

public record RequestGenreUpsert(
        Long id,
        String name,
        List<String> genres
) {
}
