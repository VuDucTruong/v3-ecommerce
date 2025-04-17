package shop.holy.v3.ecommerce.api.dto.product;

import shop.holy.v3.ecommerce.api.dto.RequestPageable;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record RequestProductSearch(
        RequestPageable pageRequest,
        long[] ids,
        List<Long> categoryIds,
        String search,
        BigDecimal priceFrom,
        BigDecimal priceTo,
        Date availableFrom,
        Date availableTo,
        boolean deleted
) {
}
