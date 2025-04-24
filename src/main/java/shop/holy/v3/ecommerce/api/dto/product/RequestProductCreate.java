package shop.holy.v3.ecommerce.api.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.api.dto.product.description.RequestProductDescription;
import shop.holy.v3.ecommerce.shared.util.SlugUtils;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Product creation request")
public record RequestProductCreate(
        RequestProductDescription productDescription,
        @Schema() String slug,
        String name,
        @Schema(description = "marking groups for variants ") Long groupId,
        MultipartFile image,
        boolean isRepresent,
        BigDecimal price,
        BigDecimal originalPrice,
        String[] tags,
        List<Long> categoryIds
) {

    public RequestProductCreate(RequestProductDescription productDescription, String slug,
                                String name, Long groupId, MultipartFile image,
                                boolean isRepresent,
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
        this.isRepresent = isRepresent;

    }
}
