package shop.holy.v3.ecommerce.api.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.api.dto.product.description.RequestProductDescription;
import shop.holy.v3.ecommerce.shared.util.SlugUtils;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Product creation request")
public record RequestProductCreate(
        @Schema(description = "HTML-formatted product description") RequestProductDescription productDescription,
        @Schema(description = "URL-friendly identifier. If not provided, will be generated from name", example = "spotify-premium") String slug,
        @Schema(example = "Spotify Premium") String name,
        @Schema(description = "ID of the variant group this product belongs to", example = "123") Long groupId,
        @Schema(description = "Product image file", format = "binary") MultipartFile image,
        @Schema(description = "Whether this product is representative of its group") boolean represent,
        @Schema(example = "9.99") BigDecimal price,
        @Schema(example = "12.99") BigDecimal originalPrice,
        @Schema(example = "[\"digital\", \"music\", \"streaming\"]") String[] tags,
        @Schema(description = "IDs of categories this product belongs to", example = "[1, 2, 3]") List<Long> categoryIds
) {

    public RequestProductCreate(RequestProductDescription productDescription, String slug,
                                String name, Long groupId, MultipartFile image,
                                boolean represent,
                                BigDecimal price, BigDecimal originalPrice,
                                String[] tags,
                                List<Long> categoryIds) {
        this.productDescription = productDescription;
        this.groupId = groupId;

        if (slug == null || slug.isBlank()) {
            this.slug = SlugUtils.INSTANCE.slugify(name);
        } else {
            this.slug = slug;
        }

        this.name = name;
        this.image = image;
        this.price = price;
        this.originalPrice = originalPrice;
        this.tags = tags;
        this.categoryIds = categoryIds;
        this.represent = represent;

    }
}