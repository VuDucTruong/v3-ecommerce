package shop.holy.v3.ecommerce.api.dto;

public record FieldErrorResponse(
        String field,
        String rejectedValue,
        String message
) {
}
