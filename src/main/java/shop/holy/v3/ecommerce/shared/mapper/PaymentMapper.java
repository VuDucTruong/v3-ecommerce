package shop.holy.v3.ecommerce.shared.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import shop.holy.v3.ecommerce.api.dto.payment.RequestPaymentCallback;
import shop.holy.v3.ecommerce.api.dto.payment.RequestPaymentUrl;
import shop.holy.v3.ecommerce.api.dto.payment.ResponsePayment;
import shop.holy.v3.ecommerce.persistence.entity.Payment;
import shop.holy.v3.ecommerce.shared.constant.PaymentStatus;
import shop.holy.v3.ecommerce.shared.constant.VnpResCodes;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PaymentMapper {
//    public static final SimpleDateFormat VNP_DATE_FMT = new SimpleDateFormat("yyyyMMddHHmmss");

    @Mapping(target = "status", constant = "PENDING", ignore = true)
    @Mapping(target = "paymentMethod", constant = "VNPAY", ignore = true)
    Payment from_UrlRequest_to_Entity(RequestPaymentUrl request);

    ResponsePayment fromEntityToResponse(Payment payment);

    default Payment from_CallbackRequest_to_Entity(RequestPaymentCallback callback) {
        Payment payment = new Payment();

        payment.setStatus(callback.vnp_TransactionStatus().equals("00")
                ? PaymentStatus.SUCCESS.name()
                : PaymentStatus.FAILED.name());
        payment.setPaymentMethod("VNPAY");
        payment.setBankCode(callback.vnp_BankCode());
        payment.setDetailCode(callback.vnp_ResponseCode());
        try {
            payment.setDetailMessage(Objects.requireNonNull(VnpResCodes.fromCode(callback.vnp_ResponseCode())).message());
        } catch (Exception _) {
            payment.setDetailMessage("Unknown Error");
        }
        payment.setNote(callback.vnp_OrderInfo());
        payment.setCardType(callback.vnp_CardType());
        payment.setTransRef(callback.vnp_TxnRef());
//        payment.setSecureHash(callback.vnp_SecureHash());
        return payment;
    }


}
