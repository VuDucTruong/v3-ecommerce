package shop.holy.v3.ecommerce.service.biz.order;


import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.payment.RequestPaymentCallback;
import shop.holy.v3.ecommerce.api.dto.payment.RequestPaymentUrl;
import shop.holy.v3.ecommerce.api.dto.payment.ResponsePayment;
import shop.holy.v3.ecommerce.persistence.entity.Payment;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_PayUrl_Status;
import shop.holy.v3.ecommerce.persistence.repository.IOrderRepository;
import shop.holy.v3.ecommerce.persistence.repository.IPaymentRepository;
import shop.holy.v3.ecommerce.persistence.repository.notification.INotificationProdKeyRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;
import shop.holy.v3.ecommerce.shared.constant.PaymentStatus;
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
public class PaymentCommand {

    private final IPaymentRepository paymentRepository;
    private final IOrderRepository orderRepository;
    private final Vnp_Pay_Properties vnpPayProperties;
    private final PaymentMapper paymentMapper;
    private final INotificationProdKeyRepository notificationRepository;

    @Transactional
    public ResponsePayment callBackPayment(RequestPaymentCallback request) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();

        Payment payment = paymentMapper.from_CallbackRequest_to_Entity(request);
        var optOrderId = paymentRepository.updatePaymentByTransRefAndSecureHash(payment);
        if (optOrderId.isEmpty())
            throw BizErrors.PAYMENT_RESULT_FAILED.exception();
        if (payment.getStatus().equals(PaymentStatus.FAILED.name())) {
            orderRepository.updateStatusById(payment.getOrderId(), OrderStatus.FAILED.name());
        } else if (payment.getStatus().equals(PaymentStatus.SUCCESS.name())) {
            String email = authAccount.getEmail();
            NotificationProdKey noti = new NotificationProdKey();
            noti.setEmail(email);
            noti.setOrderId(optOrderId.get());
            notificationRepository.save(noti);
        }
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
            Pair<String, String> pUrlPair = build_Pay_Url(p1.getBankCode(), amount, tRef, p1.getNote(), ip,req.callbackUrl());
            ///  SAVE .....
            {
                p1.setProfileId(profileId);
                p1.setTransRef(tRef);
                p1.setPaymentUrl(pUrlPair.getFirst());
                p1.setSecureHash(pUrlPair.getSecond());
                paymentRepository.save(p1);
            }
            int changes = orderRepository.updateStatusById(orderId, OrderStatus.PROCESSING.name());
            if (changes <= 0) throw BizErrors.ORDER_NOT_FOUND.exception();
            return pUrlPair.getFirst();
        }
        return opt_payment.get().getPaymentUrl();
    }

    private Pair<String, String> build_Pay_Url(String bankCode, BigDecimal amount, String transRef, String orderInfo, String ip, String returnUrl) {
        Map<String, String> vnpParamsMap = vnpPayProperties.buildParamsMap(transRef, orderInfo, returnUrl);
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
        return Pair.of(queryUrl, hashData);
    }

}
