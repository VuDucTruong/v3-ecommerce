package shop.holy.v3.ecommerce.api.dto.blog.genre;

import java.util.List;

public record RequestGenre1Upsert(
        Long id,
        String name,
        List<Long> genreIds
) {
}
