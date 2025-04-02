package shop.holy.v3.ecommerce.api.dto.payment;

import java.util.Map;

public record RequestPaymentUrl(
       long orderId,
       String address,
       String orderInfo,
       String bankCode,
       Map<String,String> highlights
) {
}
