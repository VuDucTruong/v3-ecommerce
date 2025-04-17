package shop.holy.v3.ecommerce.api.dto.order;

import shop.holy.v3.ecommerce.api.dto.RequestPageable;
import shop.holy.v3.ecommerce.shared.constant.PaymentStatus;

import java.math.BigDecimal;

public record RequestOrderSearch(
        RequestPageable pageRequest,
        String search,
        PaymentStatus status,
        BigDecimal totalFrom,
        BigDecimal totalTo,
        boolean deleted
) {
}
