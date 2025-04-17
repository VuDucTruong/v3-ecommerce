package shop.holy.v3.ecommerce.shared.exception;


public class UnAuthorisedException extends BaseBizException {
    String message;
    int code = 000;

    public UnAuthorisedException(String message) {
        super(message);
    }
}
