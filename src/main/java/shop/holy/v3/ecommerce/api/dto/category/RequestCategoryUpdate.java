package shop.holy.v3.ecommerce.api.dto.category;

import org.springframework.web.multipart.MultipartFile;

public record RequestCategoryUpdate(
        long id,
        String description,
        String name,
//        String imageUrl,
        MultipartFile image
) {

}
