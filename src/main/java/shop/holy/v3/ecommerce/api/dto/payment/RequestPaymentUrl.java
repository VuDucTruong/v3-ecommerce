package shop.holy.v3.ecommerce.api.dto.payment;

public record RequestPaymentUrl(
       long orderId,
       String bankCode,
       String note,
       String callbackUrl
) {
}
