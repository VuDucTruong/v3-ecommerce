package shop.holy.v3.ecommerce.api.dto.order;

import org.springframework.data.domain.Pageable;
import shop.holy.v3.ecommerce.shared.constant.PaymentStatus;

import java.math.BigDecimal;

public record RequestOrderSearch(
        Pageable pageRequest,
        String search,
        PaymentStatus status,
        BigDecimal totalFrom,
        BigDecimal totalTo
) {
}
