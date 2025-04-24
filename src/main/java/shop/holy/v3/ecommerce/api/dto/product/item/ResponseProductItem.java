package shop.holy.v3.ecommerce.api.dto.product.item;

public record ResponseProductItem(
        long productId,
        String productKey,
        String region,
        boolean active
) {
}
