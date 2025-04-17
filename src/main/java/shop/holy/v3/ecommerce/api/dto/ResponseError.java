package shop.holy.v3.ecommerce.api.dto;

public record ResponseError<T>(
        String path, // /users/1
        int code, // 123
        String message, //
        T data
) {
}
