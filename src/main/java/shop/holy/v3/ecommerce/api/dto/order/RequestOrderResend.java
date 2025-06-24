package shop.holy.v3.ecommerce.api.dto.order;

public record RequestOrderResend(
        Long orderId,
        String email
) {
}
