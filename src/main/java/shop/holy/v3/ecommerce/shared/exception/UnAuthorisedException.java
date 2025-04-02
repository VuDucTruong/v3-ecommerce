package shop.holy.v3.ecommerce.shared.exception;


public class UnAuthorisedException extends RuntimeException {
    public UnAuthorisedException(String message) {
        super(message);
    }
}
