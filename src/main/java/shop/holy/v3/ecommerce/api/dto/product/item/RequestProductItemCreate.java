package shop.holy.v3.ecommerce.api.dto.product.item;

public record RequestProductItemCreate(
        long productId,
        String productKey,
        String region
) {
}
