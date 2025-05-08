package shop.holy.v3.ecommerce.api.dto.order;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import shop.holy.v3.ecommerce.api.dto.coupon.ResponseCoupon;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;

public record ResponseOrder(
        long id,
        Date createdAt,
        Date deletedAt,
        OrderStatus status,
        ResponseCoupon coupon,
        BigDecimal originalAmount,
        BigDecimal amount,
        ResponseOrderDetail[] details
) {
    public record ResponseOrderDetail(
            BigDecimal price,
            BigDecimal originalPrice,
            @JsonUnwrapped ResponseOrderItem product,
            int quantity
    ) {
    }

    public record ResponseOrderItem(
            long id,
            String name,
            String imageUrl,
            String slug
    ) {
    }
}
