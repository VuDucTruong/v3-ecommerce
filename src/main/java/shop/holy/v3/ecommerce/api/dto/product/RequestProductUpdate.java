package shop.holy.v3.ecommerce.api.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.api.dto.product.description.RequestProductDescription;

import java.math.BigDecimal;
import java.util.List;

public record RequestProductUpdate(
        @Schema(example = "1234") long id,
        @Schema(description = "HTML-formatted product description") RequestProductDescription productDescription,
        @Schema(description = "URL-friendly identifier", example = "spotify-premium") String slug,
        @Schema(example = "Spotify Premium") String name,
        @Schema(description = "ID of the variant group", example = "123") Long groupId,
        @Schema(description = "New product image file", format = "binary") MultipartFile image,
        @Schema(example = "9.99") BigDecimal price,
        @Schema(example = "12.99") BigDecimal originalPrice,
        @Schema(description = "Whether this product is representative of its group") boolean represent,
        List<Long> categoryIds

) {
}