package shop.holy.v3.ecommerce.api.dto.product;

public record RequestProductDescription(
        String description,
        String info,
        String platform,
        String policy,
        String tutorial

) {
}
