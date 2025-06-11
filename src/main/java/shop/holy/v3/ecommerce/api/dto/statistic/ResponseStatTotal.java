package shop.holy.v3.ecommerce.api.dto.statistic;

import java.math.BigDecimal;

public record ResponseStatTotal(
        int totalNewCustomers,
        BigDecimal averageOrderValue,
        BigDecimal revenue,
        long totalOrders ///  FROM - TO
) {
}
