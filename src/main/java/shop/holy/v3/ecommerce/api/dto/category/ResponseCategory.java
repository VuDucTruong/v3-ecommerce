package shop.holy.v3.ecommerce.api.dto.category;

public record ResponseCategory(
        long id,
        String name,
        String description,
        String imageUrl
) {
}
