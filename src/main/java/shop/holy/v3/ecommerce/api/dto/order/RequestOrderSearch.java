package shop.holy.v3.ecommerce.api.dto.order;

import shop.holy.v3.ecommerce.api.dto.RequestPageable;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public record RequestOrderSearch(
        RequestPageable pageRequest,
        ArrayList<Long> ids,
        String search,
        String status,
        List<String> statuses,
        BigDecimal totalFrom,
        BigDecimal totalTo,
        boolean deleted
) {
}
