package shop.holy.v3.ecommerce.api.dto.category;

import java.util.Date;

public record ResponseCategory(
        long id,
        String name,
        String description,
        String imageUrl,
        Date deletedAt) {
}
