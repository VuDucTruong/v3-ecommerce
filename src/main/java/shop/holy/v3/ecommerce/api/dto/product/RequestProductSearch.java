package shop.holy.v3.ecommerce.api.dto.product;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

public record RequestProductSearch(
        Pageable pageRequest,
        Set<Long> ids,
        List<Long> categoryIds,
        String search,
        BigDecimal priceFrom,
        BigDecimal priceTo,
        Double discountPercentFrom,
        Double discountPercentTo,
        Date availableFrom,
        Date availableTo
) {
}
