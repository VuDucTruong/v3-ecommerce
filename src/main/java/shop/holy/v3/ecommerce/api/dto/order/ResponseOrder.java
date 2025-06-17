package shop.holy.v3.ecommerce.api.dto.order;

import shop.holy.v3.ecommerce.api.dto.coupon.ResponseCoupon;
import shop.holy.v3.ecommerce.api.dto.payment.ResponsePayment;
import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;

public record ResponseOrder(
        long id,
        Date createdAt,
        Date deletedAt,
        OrderStatus status,
        ResponseProfile.Detailed profile,
        ResponseCoupon coupon,
        ResponsePayment payment,
        BigDecimal originalAmount,
        BigDecimal amount,
        ResponseOrderDetail[] details,
        String sentMail
) {
    public record ResponseOrderDetail(
            Long id,
            BigDecimal price,
            BigDecimal originalPrice,
            ResponseOrderItem product,
            long quantity
    ) {
    }

    public record ResponseOrderItem(
            long id,
            String name,
            String imageUrl,
            String slug,
            long quantity,
            String[] tags,
            String key
    ) {
    }

}
