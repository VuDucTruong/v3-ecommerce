package shop.holy.v3.ecommerce.api.dto.statistic;

import java.math.BigDecimal;

public record ResponseStatTotal(
        int customerCount,
        long soldCount,
        BigDecimal revenue,
        long orderCount
) {
}
