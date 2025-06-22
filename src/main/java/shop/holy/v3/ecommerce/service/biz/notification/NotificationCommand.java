package shop.holy.v3.ecommerce.service.biz.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys;
import shop.holy.v3.ecommerce.api.dto.order.RequestOrderResend;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKeyFail;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKeySuccess;
import shop.holy.v3.ecommerce.persistence.repository.IOrderRepository;
import shop.holy.v3.ecommerce.persistence.repository.notification.INotificationProdKeyFailedRepository;
import shop.holy.v3.ecommerce.persistence.repository.notification.INotificationProdKeyRepository;
import shop.holy.v3.ecommerce.persistence.repository.notification.INotificationProdKeySuccess;
import shop.holy.v3.ecommerce.service.biz.product.item.ProductItemCommand;
import shop.holy.v3.ecommerce.service.smtp.SmtpService;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationCommand {
    private final INotificationProdKeyRepository notiRepository;
    private final INotificationProdKeyFailedRepository failedNotiRepository;
    private final INotificationProdKeySuccess successNotiRepository;
    private final IOrderRepository orderRepository;
    private final ProductItemCommand productItemCommand;
    private final ObjectMapper objectMapper;
    private final SmtpService smtpService;

    @Transactional
    public void handleRetry1(long notiId, long orderId) {
        notiRepository.updateRetry1ById(notiId);
        orderRepository.updateStatusById(orderId, OrderStatus.RETRY_1.name());
    }


    @Transactional
    public void handleFailed(NotificationProdKey key, String reason) {
        NotificationProdKeyFail keyFailed = new NotificationProdKeyFail();
        keyFailed.setEmail(key.getEmail());
        keyFailed.setOrderId(key.getOrderId());
        keyFailed.setRetry1(key.getRetry1());
        keyFailed.setRetry2(key.getRetry2());
        keyFailed.setReason(reason);
        long orderId = key.getOrderId();
        notiRepository.deleteById(key.getId());
        orderRepository.updateStatusById(orderId, OrderStatus.FAILED_MAIL.name());
        failedNotiRepository.save(keyFailed);
    }

    @Transactional
    public void handleSuccess(NotificationProdKey key, MailProductKeys mailProductKeys) {
        long orderId = key.getOrderId();
        orderRepository.updateStatusById(orderId, OrderStatus.COMPLETED.name());

        try {
            NotificationProdKeySuccess notiSuccess = new NotificationProdKeySuccess();
            notiSuccess.setEmail(key.getEmail());
            notiSuccess.setOrderId(key.getOrderId());
            notiSuccess.setRetry1(key.getRetry1());
            notiSuccess.setRetry2(key.getRetry2());
            String sentMetaContent = objectMapper.writeValueAsString(mailProductKeys);
            notiSuccess.setMailProdKeyJson(sentMetaContent);
            long[] flattenedProductItem_Ids = mailProductKeys.getMetas().values().stream()
                    .flatMap(meta -> meta.getKeys().stream())
                    .mapToLong(MailProductKeys.ProductKey::getId)
                    .toArray();

            notiRepository.deleteById(key.getId());
            productItemCommand.markItemUsed(flattenedProductItem_Ids);
            successNotiRepository.save(notiSuccess);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void resendFailedNotification(RequestOrderResend request) {
        boolean exist = failedNotiRepository.existsByOrderId(request.orderId());
        if (!exist)
            throw BizErrors.ORDER_NOT_FOUND.exception();
        NotificationProdKey noti = new NotificationProdKey();
        noti.setOrderId(request.orderId());
        noti.setEmail(request.email());
        notiRepository.save(noti);
    }

    public void resendSuccessNotification(RequestOrderResend request) {
        Optional<String> rawMailProductKeys = successNotiRepository.findFirstMailProdKeyJsonById(request.orderId());
        MailProductKeys mpk = rawMailProductKeys.map(str -> {
            try {
                return objectMapper.readValue(str, MailProductKeys.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).orElseThrow(BizErrors.ORDER_NOT_FOUND::exception);
        try {
            smtpService.sendMailProductKeys(mpk);
        } catch (MessagingException e) {
            throw BizErrors.SMTP_FAILED.exception();
        }
    }


    public void resendFailedNotifications(Collection<Long> ids) {
        List<NotificationProdKeyFail> fails = failedNotiRepository.findAllByIdIn(ids);
        fails.forEach(f -> {
            f.setCreatedAt(null);
            f.setRetry1(null);
            f.setRetry2(null);
        });
        List<NotificationProdKey> notis = new ArrayList<>(fails);
        notiRepository.saveAll(notis);
    }


}
