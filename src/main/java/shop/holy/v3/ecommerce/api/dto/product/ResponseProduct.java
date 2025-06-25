package shop.holy.v3.ecommerce.api.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import shop.holy.v3.ecommerce.api.dto.category.ResponseCategory;
import shop.holy.v3.ecommerce.api.dto.product.description.ResponseDescription;
import shop.holy.v3.ecommerce.shared.constant.ProductStatus;

import java.math.BigDecimal;
import java.util.Objects;

public record ResponseProduct(
        @Schema(example = "1234") long id,
        @Schema(description = "each inner fields are HTML") ResponseDescription productDescription,
        @Schema(example = "spotify-12-months") String slug,
        @Schema(example = "Spotify Premium") String name,
        @Schema(example = "https://example.com/images/spotify.jpg") String imageUrl,
        @Schema(example = "[\"digital\", \"music\", \"streaming\"]") String[] tags,
        @Schema(description = """
                Whether this product is representative of its group,
                1 product -> 1 group -> N represent Products
                """) boolean represent,
        Boolean favorite,
        long groupId,
        @Schema(example = "9.99") BigDecimal price,
        @Schema(example = "12.99") BigDecimal originalPrice,
        @Schema(example = "100") long quantity,
        ResponseCategory[] categories,
        @Schema(example = "IN_STOCK") ProductStatus status,
//        @Schema(example = "2023-01-01T00:00:00Z") Date deletedAt,
        ResponseProductMetadata[] variants
) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ResponseProduct that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}