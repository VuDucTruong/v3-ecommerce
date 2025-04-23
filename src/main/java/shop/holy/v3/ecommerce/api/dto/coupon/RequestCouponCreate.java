package shop.holy.v3.ecommerce.api.dto.coupon;

import shop.holy.v3.ecommerce.shared.constant.CouponType;

import java.math.BigDecimal;
import java.util.Date;

public record RequestCouponCreate(
        String code,
        CouponType type,
        Date availableFrom,
        Date availableTo,
        BigDecimal value,
        BigDecimal minAmount,
        Long maxAppliedAmount,
        int usageLimit,
        String description
) {

}
