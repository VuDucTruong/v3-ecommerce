package shop.holy.v3.ecommerce.api.dto.coupon;

import shop.holy.v3.ecommerce.shared.constant.CouponType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public record ResponseCoupon(
        long id,
        String code,
        CouponType type,
        Date deletedAt,
        LocalDate availableFrom,
        LocalDate availableTo,
        BigDecimal value,
        BigDecimal minAmount,
        Long maxAppliedAmount,
        int usageLimit,
        String description
) {
}