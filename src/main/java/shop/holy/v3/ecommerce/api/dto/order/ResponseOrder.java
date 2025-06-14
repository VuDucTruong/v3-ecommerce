package shop.holy.v3.ecommerce.api.dto.order;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import shop.holy.v3.ecommerce.api.dto.coupon.ResponseCoupon;
import shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record ResponseOrder(
        long id,
        Date createdAt,
        Date deletedAt,
        OrderStatus status,
        ResponseCoupon coupon,
        BigDecimal originalAmount,
        BigDecimal amount,
        ResponseOrderDetail[] details,
        MailProductKeys sentMail
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
            String[] tags
    ) {
    }
}
