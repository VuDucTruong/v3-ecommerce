package shop.holy.v3.ecommerce.api.dto.coupon;

import shop.holy.v3.ecommerce.shared.constant.CouponType;

import java.math.BigDecimal;
import java.util.Date;

public record RequestCouponCreate(
        String search,
        CouponType type,
        Date availableFrom,
        Date availableTo,
        BigDecimal value,
        Integer minQTY,
        BigDecimal minAmount,
        Long maxAppliedAmount,
        Integer usageLimit
) {

}
