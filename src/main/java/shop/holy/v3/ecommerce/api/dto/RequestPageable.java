package shop.holy.v3.ecommerce.api.dto;

public record RequestPageable(
        int page,
        int size,
        String sortBy,
        String sortDirection
) {
}
