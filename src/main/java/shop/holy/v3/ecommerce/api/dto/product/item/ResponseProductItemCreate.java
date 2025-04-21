package shop.holy.v3.ecommerce.api.dto.product.item;

public record ResponseProductItemCreate(
        String[] inserted,
        String[] rejected
) {
}
