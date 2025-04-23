package shop.holy.v3.ecommerce.api.dto.order;

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
        BigDecimal totalValue
) {
    public record ResponseOrderDetail(

    ) {

    }
}
