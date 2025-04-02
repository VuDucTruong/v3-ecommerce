package shop.holy.v3.ecommerce.api.dto.order;

import java.util.List;

public record RequestOrderCreate(
        String couponCode,
        List<RequestOrderDetail> orderDetails
) {

    public record RequestOrderDetail(
            String productId,
            int quantity
    ) {}
}
