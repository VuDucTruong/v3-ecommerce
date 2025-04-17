package shop.holy.v3.ecommerce.shared.exception;

public class BaseBizException extends RuntimeException {
    public BaseBizException() {
        super("Base business exception occurred", null, false, false);
    }

    public BaseBizException(String message) {
        super(message, null, false, false);
    }

    public BaseBizException(String message, Throwable cause) {
        super(message, cause, false, false);
    }

    public BaseBizException(Throwable cause) {
        super(cause.getMessage(), cause, false, false);
    }
}
