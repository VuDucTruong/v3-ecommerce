package shop.holy.v3.ecommerce.api.dto.product.group;

public record RequestProductGroupUpdate(
        long id,
        Long representId,
        String name
) {
}
