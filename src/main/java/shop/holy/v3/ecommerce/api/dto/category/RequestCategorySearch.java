package shop.holy.v3.ecommerce.api.dto.category;

import org.springframework.data.domain.Pageable;

public record RequestCategorySearch(
        Pageable pageRequest,
        String search
) {
}
