package shop.holy.v3.ecommerce.api.dto.coupon;

import shop.holy.v3.ecommerce.shared.constant.CouponType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResponseCoupon(
        long id,
        String code,
        CouponType type,
        LocalDate availableFrom,
        LocalDate availableTo,
        BigDecimal value,
        BigDecimal minAmount,
        Long maxAppliedAmount,
        int usageLimit,
        String description
) {
}