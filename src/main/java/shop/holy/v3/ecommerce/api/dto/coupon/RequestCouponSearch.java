package shop.holy.v3.ecommerce.api.dto.coupon;

import org.springframework.data.domain.Pageable;
import shop.holy.v3.ecommerce.shared.constant.CouponType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RequestCouponSearch(
        Pageable pageRequest,
        String search,
        CouponType type,
        LocalDate availableFrom,
        LocalDate availableTo,
        BigDecimal valueFrom,
        BigDecimal valueTo,
        Integer minQTYFrom,
        Integer minQTYTo
) {
}
