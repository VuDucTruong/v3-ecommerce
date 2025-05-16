package shop.holy.v3.ecommerce.api.dto.payment;

public record RequestPaymentCallback(
//        long vnp_Amount,
        String vnp_BankCode,
        String vnp_CardType,
        String vnp_OrderInfo,
//        String vnp_PayDate,
        String vnp_ResponseCode,
//        String vnp_TmnCode,
//        String vnp_TransactionNo,
        String vnp_TransactionStatus,
        String vnp_TxnRef,
//        String vnp_BankTranNo,
        String vnp_SecureHash

) {
}
