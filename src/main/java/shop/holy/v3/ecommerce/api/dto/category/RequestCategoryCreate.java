package shop.holy.v3.ecommerce.api.dto.category;

import org.springframework.web.multipart.MultipartFile;

public record RequestCategoryCreate(
        String description,
        String name,
        MultipartFile image
) {
}
