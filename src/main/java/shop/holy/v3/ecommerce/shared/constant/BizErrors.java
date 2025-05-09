package shop.holy.v3.ecommerce.shared.constant;

import lombok.RequiredArgsConstructor;
import shop.holy.v3.ecommerce.shared.exception.*;

@RequiredArgsConstructor
public enum BizErrors {

    LOGIN_FAILED(new UnAuthorisedException("Invalid username or password")),
    INVALID_TOKEN(new UnAuthorisedException("Invalid token")),
    PAYMENT_RESULT_FAILED(new BadRequestException("Payment update failed, please try again")),

    EMAIL_ALREADY_EXISTS(new BadRequestException("Email already exists")),
    INVALID_EMAIL(new BadRequestException("Invalid email format")),

    ACCOUNT_NOT_FOUND(new ResourceNotFoundException("Account not found")),
    PRODUCT_NOT_FOUND(new ResourceNotFoundException("Product not found")),
    ORDER_NOT_FOUND(new ResourceNotFoundException("Order not found")),
    ORDER_AMOUNT_TOO_MINOR(new BadRequestException("Order amount is too small")),
    OTP_NOT_FOUND(new ResourceNotFoundException("OTP not found")),
    RESOURCE_NOT_FOUND(new ResourceNotFoundException("Resource not found")),

    AUTHORISATION_NULL(new UnAuthorisedException("Authorisation is not found")),
    AUTHORISATION_ANNONYMOUS(new UnAuthorisedException("Authorisation user is anonymous")),
    AUTHORISATION_INVALID(new UnAuthorisedException("Authorisation is invalid")),

    INVALID_COUPON(new BadRequestException("Coupon not available")),
    FORBIDDEN_ACTION(new ForbiddenException("You don't have permission to do this action")),
    RESOURCE_NOT_OWNED(new ForbiddenException("You don't own this resource")),
    INVALID_DATE_FORMAT(new BadRequestException("Invalid date format. Please use yyyy-MM-dd'T'HH:mm:ss.SSSX"));


    private final BaseBizException exception;

    public BaseBizException exception() {
        return exception;
    }

}
