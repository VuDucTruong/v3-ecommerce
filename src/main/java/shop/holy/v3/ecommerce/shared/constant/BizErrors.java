package shop.holy.v3.ecommerce.shared.constant;

import lombok.RequiredArgsConstructor;
import shop.holy.v3.ecommerce.shared.exception.*;

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
    ORDER_NOT_FOUND(new ResourceNotFoundException("Order not found")),
    OTP_NOT_FOUND(new ResourceNotFoundException("OTP not found")),


    PRODUCT_OUT_OF_STOCK(new BadRequestException("Product out of stock")),
    RESOURCE_NOT_FOUND(new ResourceNotFoundException("Resource not found")),

    AUTHORISATION_NULL(new UnAuthorisedException("Authorisation null")),
    AUTHORISATION_ANNONYMOUS(new UnAuthorisedException("Authorisation user is anonymous")),
    AUTHORISATION_INVALID(new UnAuthorisedException("Authorisation invalid")),

    CART_EMPTY(new BadRequestException("Cart is empty")),
    PAYMENT_FAILED(new BadRequestException("Payment failed")),
    INVALID_COUPON(new BadRequestException("Invalid coupon")),
    COUPON_EXPIRED(new BadRequestException("Coupon expired")),
    COUPON_ALREADY_USED(new BadRequestException("Coupon already used")),
    INVALID_ADDRESS(new BadRequestException("Invalid address")),
    ADDRESS_NOT_FOUND(new ResourceNotFoundException("Address not found")),
    INVALID_PHONE_NUMBER(new BadRequestException("Invalid phone number")),
    PHONE_NUMBER_ALREADY_EXISTS(new BadRequestException("Phone number already exists")),
    FORBIDDEN_ACTION(new ForbiddenException("You don't have permission to do this action")),
    INVALID_DATE(new BadRequestException("Invalid date format"));

    private final BaseBizException exception;

    public BaseBizException exception() {
        return exception;
    }

}
