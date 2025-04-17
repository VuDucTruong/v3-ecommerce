package shop.holy.v3.ecommerce.api.dto.order;

import java.util.List;
import java.util.Map;

public record RequestOrderCreate(
        String couponCode,
        Map<String, String> requestInfo,
        List<RequestOrderDetail> orderDetails
) {

    public record RequestOrderDetail(
            long productId,
            int quantity
    ) {}
}
