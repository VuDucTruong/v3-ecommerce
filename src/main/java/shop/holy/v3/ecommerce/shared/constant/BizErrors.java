package shop.holy.v3.ecommerce.shared.constant;

import lombok.RequiredArgsConstructor;
import shop.holy.v3.ecommerce.shared.exception.*;

@RequiredArgsConstructor
public enum BizErrors {

    LOGIN_FAILED(new UnAuthorisedException("Invalid username or password")),
    INVALID_TOKEN(new UnAuthorisedException("Invalid token")),
    PAYMENT_RESULT_FAILED(new BadRequestException("Payment update failed, please try again")),
    SMTP_FAILED(new BadRequestException("Error sending email, please try again later")),

    EMAIL_ALREADY_EXISTS(new BadRequestException("Email already exists")),
    INVALID_EMAIL(new BadRequestException("Invalid email format")),
    INVALID_OTP(new BadRequestException("OTP not found")),
    OTP_EXPIRED(new BadRequestException("OTP is expired")),

    ACCOUNT_NOT_FOUND(new ResourceNotFoundException("Account not found")),
    BLOG_NOT_FOUND(new ResourceNotFoundException("Blog not found")),
    PRODUCT_NOT_FOUND(new ResourceNotFoundException("Product not found")),
    ORDER_NOT_FOUND(new ResourceNotFoundException("Order not found")),
    CATEGORY_NOT_FOUND(new ResourceNotFoundException("Category not found")),
    ORDER_AMOUNT_TOO_MINOR(new BadRequestException("Order amount is too small")),
    RESOURCE_NOT_FOUND(new ResourceNotFoundException("Resource not found")),

    AUTHORISATION_NULL(new UnAuthorisedException("Authorisation is not found")),
    AUTHORISATION_ANNONYMOUS(new UnAuthorisedException("Authorisation user is anonymous")),
    AUTHORISATION_INVALID(new UnAuthorisedException("Authorisation is invalid")),
    AUTHORISATION_INVALID_REQUIRE_REFRESH(new UnAuthorisedException("Authorisation is invalid, please login again!")),

    INVALID_COUPON(new BadRequestException("Coupon not available")),
    FORBIDDEN_ACTION(new ForbiddenException("You don't have permission to do this action")),
    FORBIDDEN_NOT_VERIFIED(new ForbiddenException("You are not verified")),
    FORBIDDEN_UNCOMPLETED_VERIFICATION(new ForbiddenException("You are having uncompleted verification, please continue or request a new OTP")),
    RESOURCE_NOT_AUTHORISED_REQUIRE_HIGHER_POWER(new ForbiddenException("Không được phép thay đổi tài nguyên này, yêu cầu tài khoản có thẩm quyền cao hơn")),
    RESOURCE_NOT_OWNED(new ForbiddenException("You don't own this resource")),

    INSUFFICIENT_PRODUCT_ITEMS(new BaseBizException("Insufficient product items")),
    INVALID_IMAGE_URL(new BadRequestException("Image upload not accepted")),
    INVALID_DATE_FORMAT(new BadRequestException("Invalid date format. Please use yyyy-MM-dd'T'HH:mm:ss.SSSX"));

    private final BaseBizException exception;

    public BaseBizException exception() {
        return exception;
    }

}
