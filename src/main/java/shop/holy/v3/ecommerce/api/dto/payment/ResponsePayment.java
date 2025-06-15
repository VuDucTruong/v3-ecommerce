package shop.holy.v3.ecommerce.api.dto.payment;

import shop.holy.v3.ecommerce.shared.constant.PaymentMethod;
import shop.holy.v3.ecommerce.shared.constant.PaymentStatus;

public record ResponsePayment(
        long id,
        long orderId,
        long profileId,
        PaymentStatus status,
        PaymentMethod paymentMethod,
        String bankCode,
        String detailCode,
        String detailMessage,
        String note,
        String cardType,
        String transRef,
        String secureHash


) {
}
