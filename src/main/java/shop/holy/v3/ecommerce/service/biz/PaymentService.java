package shop.holy.v3.ecommerce.service.biz;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.payment.RequestPaymentCallback;
import shop.holy.v3.ecommerce.api.dto.payment.RequestPaymentUrl;
import shop.holy.v3.ecommerce.api.dto.payment.ResponsePayment;
import shop.holy.v3.ecommerce.persistence.entity.Payment;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_PayUrl_Status;
import shop.holy.v3.ecommerce.persistence.repository.IOrderRepository;
import shop.holy.v3.ecommerce.persistence.repository.IPaymentRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.mapper.PaymentMapper;
import shop.holy.v3.ecommerce.shared.property.Vnp_Pay_Properties;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;
import shop.holy.v3.ecommerce.shared.util.VNPayUtil;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IPaymentRepository paymentRepository;
    private final IOrderRepository orderRepository;
    private final Vnp_Pay_Properties vnpPayProperties;
    private final PaymentMapper paymentMapper;

    @Transactional
    public ResponsePayment callBackPayment(RequestPaymentCallback request) {
        Payment payment = paymentMapper.from_CallbackRequest_to_Entity(request);
        int changes = paymentRepository.updatePaymentByTransRefAndSecureHash(payment);
        if (changes <= 0)
            throw BizErrors.PAYMENT_RESULT_FAILED.exception();
        return paymentMapper.fromEntityToResponse(payment);
    }

    @Transactional
    public String upsertVnpPayment(RequestPaymentUrl req, String ip) {
        long orderId = req.orderId();
        long profileId = SecurityUtil.getAuthNonNull().getProfileId();

        Optional<ProQ_PayUrl_Status> opt_payment = paymentRepository.findFirstPaymentUrlAndStatusByOrderId(orderId);

        if (opt_payment.isEmpty()) {
            Payment p1 = paymentMapper.from_UrlRequest_to_Entity(req);
            BigDecimal amount = orderRepository.findAmountByOrderId(orderId).orElseThrow(BizErrors.ORDER_NOT_FOUND::exception);
            String tRef = UUID.randomUUID().toString();
            String paymentUrl = build_Pay_Url(p1.getBankCode(), amount, tRef, p1.getNote(), ip);
            ///  SAVE .....
            {
                p1.setProfileId(profileId);
                p1.setTransRef(tRef);
                p1.setPaymentUrl(paymentUrl);
                paymentRepository.save(p1);
            }
            return paymentUrl;
        }
        return opt_payment.get().getPaymentUrl();
    }

    private String build_Pay_Url(String bankCode, BigDecimal amount, String transRef, String orderInfo, String ip) {
        Map<String, String> vnpParamsMap = vnpPayProperties.buildParamsMap(transRef, orderInfo);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount.longValue() * 100));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", ip);
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnpPayProperties.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        return vnpPayProperties.getVnp_PayUrl() + "?" + queryUrl;
    }

}
