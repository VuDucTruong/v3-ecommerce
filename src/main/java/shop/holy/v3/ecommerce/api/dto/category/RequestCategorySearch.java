package shop.holy.v3.ecommerce.api.dto.category;

import shop.holy.v3.ecommerce.api.dto.RequestPageable;

public record RequestCategorySearch(
        RequestPageable pageRequest,
        String search,
        boolean deleted
) {
}
