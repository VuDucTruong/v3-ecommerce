package shop.holy.v3.ecommerce.api.dto.order;

import lombok.Getter;
import lombok.Setter;
import shop.holy.v3.ecommerce.api.dto.coupon.ResponseCoupon;
import shop.holy.v3.ecommerce.api.dto.payment.ResponsePayment;
import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        String sentMail,
        String reason
) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ResponseOrder that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ResponseOrder{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", deletedAt=" + deletedAt +
                ", status=" + status +
                ", profile=" + profile +
                ", coupon=" + coupon +
                ", payment=" + payment +
                ", originalAmount=" + originalAmount +
                ", amount=" + amount +
                ", details=" + Arrays.toString(details) +
                ", sentMail='" + sentMail + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    public record ResponseOrderDetail(
            Long id,
            BigDecimal price,
            BigDecimal originalPrice,
            ResponseOrderItem product,
            long quantity
    ) {
    }

    @Getter
    @Setter
    public static class ResponseOrderItem {
        long id;
        String name;
        String imageUrl;
        String slug;
        long quantity;
        String[] tags;
        List<String> keys;

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof ResponseOrderItem that)) return false;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id);
        }
    }

}
