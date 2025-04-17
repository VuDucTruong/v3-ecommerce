package shop.holy.v3.ecommerce.shared.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BizErrors {
    LOGIN_FAILED("Login failed", 1001),
    USER_NOT_FOUND("User not found", 1002),
    INVALID_PASSWORD("Invalid password", 1003),
    USERNAME_ALREADY_EXISTS("Username already exists", 1004),
    EMAIL_ALREADY_EXISTS("Email already exists", 1005),
    INVALID_EMAIL("Invalid email format", 1006),
    PASSWORD_TOO_WEAK("Password is too weak", 1007),
    PRODUCT_NOT_FOUND("Product not found", 2001),
    PRODUCT_OUT_OF_STOCK("Product out of stock", 2002),
    CART_EMPTY("Cart is empty", 3001),
    ORDER_NOT_FOUND("Order not found", 4001),
    PAYMENT_FAILED("Payment failed", 5001),
    INVALID_COUPON("Invalid coupon", 6001),
    COUPON_EXPIRED("Coupon expired", 6002),
    COUPON_ALREADY_USED("Coupon already used", 6003),
    INVALID_ADDRESS("Invalid address", 7001),
    ADDRESS_NOT_FOUND("Address not found", 7002),
    INVALID_PHONE_NUMBER("Invalid phone number", 8001),
    PHONE_NUMBER_ALREADY_EXISTS("Phone number already exists", 8002),
    INVALID_DATE("Invalid date format", 9001);


    private final String message;
    private final int code;

}
