package shop.holy.v3.ecommerce.api.dto.product.item;

public record RequestProductItemUpdate(
        long productId,
        String productKey,
        String region,
        boolean active
) {
}
