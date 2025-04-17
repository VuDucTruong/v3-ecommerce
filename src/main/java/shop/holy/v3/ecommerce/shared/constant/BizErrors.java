package shop.holy.v3.ecommerce.shared.constant;

import lombok.RequiredArgsConstructor;
import shop.holy.v3.ecommerce.shared.exception.BadRequestException;
import shop.holy.v3.ecommerce.shared.exception.BaseBizException;
import shop.holy.v3.ecommerce.shared.exception.ResourceNotFoundException;
import shop.holy.v3.ecommerce.shared.exception.UnAuthorisedException;

@RequiredArgsConstructor
public enum BizErrors {

    LOGIN_FAILED(new UnAuthorisedException("Login failed")),
    USER_NOT_FOUND(new ResourceNotFoundException("User not found")),
    INVALID_PASSWORD(new UnAuthorisedException("Invalid password")),
    USERNAME_ALREADY_EXISTS(new BadRequestException("Username already exists")),

    EMAIL_ALREADY_EXISTS(new ResourceNotFoundException("Email already exists")),
    INVALID_EMAIL(new BadRequestException("Invalid email format")),
    PASSWORD_TOO_WEAK(new BadRequestException("Password is too weak")),

    ACCOUNT_NOT_FOUND(new ResourceNotFoundException("Account not found")),
    PRODUCT_NOT_FOUND(new ResourceNotFoundException("Product not found")),
    PRODUCT_OUT_OF_STOCK(new BadRequestException("Product out of stock")),
    OTP_NOT_FOUND(new ResourceNotFoundException("OTP not found")),

    AUTHORISATION_NULL(new UnAuthorisedException("Authorisation null")),
    AUTHORISATION_ANNONYMOUS(new UnAuthorisedException("Authorisation user is anonymous")),
    AUTHORISATION_INVALID(new UnAuthorisedException("Authorisation invalid")),

    CART_EMPTY(new BadRequestException("Cart is empty")),
    ORDER_NOT_FOUND(new ResourceNotFoundException("Order not found")),
    PAYMENT_FAILED(new BadRequestException("Payment failed")),
    INVALID_COUPON(new BadRequestException("Invalid coupon")),
    COUPON_EXPIRED(new BadRequestException("Coupon expired")),
    COUPON_ALREADY_USED(new BadRequestException("Coupon already used")),
    INVALID_ADDRESS(new BadRequestException("Invalid address")),
    ADDRESS_NOT_FOUND(new ResourceNotFoundException("Address not found")),
    INVALID_PHONE_NUMBER(new BadRequestException("Invalid phone number")),
    PHONE_NUMBER_ALREADY_EXISTS(new BadRequestException("Phone number already exists")),
    INVALID_DATE(new BadRequestException("Invalid date format"));

    private final BaseBizException exception;

    public BaseBizException exception() {
        return exception;
    }

}
