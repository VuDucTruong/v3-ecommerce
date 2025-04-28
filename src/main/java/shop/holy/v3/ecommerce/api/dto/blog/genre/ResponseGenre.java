package shop.holy.v3.ecommerce.api.dto.blog.genre;

public record ResponseGenre(
        long id,
        String name,
        String[] genres
) {
}
