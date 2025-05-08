package shop.holy.v3.ecommerce.api.dto.account;

public record RequestPasswordUpdate(String email, String otp, String password) {
}
