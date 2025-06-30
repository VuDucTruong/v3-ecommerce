package shop.holy.v3.ecommerce.shared.exception;

public class BaseBizException extends RuntimeException {
    public BaseBizException() {
        super("Base business exception occurred", null, false, true);
    }

    public BaseBizException(String message) {
        super(message, null, false, true);
    }

    public BaseBizException(String message, Throwable cause) {
        super(message, cause, false, true);
    }

    public BaseBizException(Throwable cause) {
        super(cause.getMessage(), cause, false, true);
    }
}
