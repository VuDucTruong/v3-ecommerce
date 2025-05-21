package shop.holy.v3.ecommerce.api.dto.blog.genre;

import java.util.List;

public record ResponseGenre1(
        long id,
        String name,
        List<ResponseGenre2> genres
) {
    public record ResponseGenre2(
            long id,
            String name
    ){}
}
