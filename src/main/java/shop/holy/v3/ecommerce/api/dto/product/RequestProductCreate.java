package shop.holy.v3.ecommerce.api.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.shared.util.SlugUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record RequestProductCreate(
        RequestProductDescription productDescription,
        String slug,
        String name,
        @Schema(description = "marking parent || children ") Long parentId,
        MultipartFile image,
        String description,
        BigDecimal price,
        BigDecimal originalPrice,
        Date availableFrom,
        Date availableTo,
        List<Long> categoryIds
) {
    public RequestProductCreate(RequestProductDescription productDescription, String slug, String name,Long parentId, MultipartFile image, String description, BigDecimal price, BigDecimal originalPrice, Date availableFrom, Date availableTo, List<Long> categoryIds) {
        this.productDescription = productDescription;
        this.parentId = parentId;

        if (slug == null || slug.isBlank()) {
            this.slug = SlugUtils.INSTANCE.slugify(name);
        } else {
            this.slug = slug;
        }

        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.originalPrice = originalPrice;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
        this.categoryIds = categoryIds;
    }
}
