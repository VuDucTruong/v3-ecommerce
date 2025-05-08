package shop.holy.v3.ecommerce.api.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record RequestOrderCreate(
        String couponCode,
        @Schema(description = """
                {
                "email": "phong@mail.com"
                "p": "01234569"
                }
                ==> required info to obtain productKey via email
                """)
        HashMap<String, String> requestInfo,
        List<RequestOrderDetail> orderDetails
) {

    public record RequestOrderDetail(
            long productId,
            int quantity
    ) {
    }
}
