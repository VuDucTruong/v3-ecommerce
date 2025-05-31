package shop.holy.v3.ecommerce.api.dto.account;

public record RequestMailVerification(
        String email, String otp
) {
}
