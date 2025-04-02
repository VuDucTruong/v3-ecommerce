package shop.holy.v3.ecommerce.api.dto.payment;

import shop.holy.v3.ecommerce.shared.constant.PaymentMethod;
import shop.holy.v3.ecommerce.shared.constant.PaymentStatus;

import java.math.BigDecimal;

public record ResponsePayment(
        long id,
        PaymentStatus status,
        long orderId,
        long profileId,
        PaymentMethod paymentMethod,
        BigDecimal amount,
        String currency,
        String bankCode,
        String orderInfo,
        String cardInfo
) {
}
