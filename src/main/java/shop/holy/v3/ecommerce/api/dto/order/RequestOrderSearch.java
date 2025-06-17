package shop.holy.v3.ecommerce.api.dto.order;

import shop.holy.v3.ecommerce.api.dto.RequestPageable;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;

import java.math.BigDecimal;
import java.util.ArrayList;

public record RequestOrderSearch(
        RequestPageable pageRequest,
        ArrayList<Long> ids,
        String search,
        OrderStatus status,
        BigDecimal totalFrom,
        BigDecimal totalTo,
        boolean deleted
) {
}
