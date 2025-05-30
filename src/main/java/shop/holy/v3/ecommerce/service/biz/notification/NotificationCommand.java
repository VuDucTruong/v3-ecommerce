package shop.holy.v3.ecommerce.service.biz.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKeyFail;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKeySuccess;
import shop.holy.v3.ecommerce.persistence.repository.IOrderRepository;
import shop.holy.v3.ecommerce.persistence.repository.notification.INotificationProdKeyFailedRepository;
import shop.holy.v3.ecommerce.persistence.repository.notification.INotificationProdKeyRepository;
import shop.holy.v3.ecommerce.persistence.repository.notification.INotificationProdKeySuccess;
import shop.holy.v3.ecommerce.service.biz.product.item.ProductItemCommand;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationCommand {
    private final INotificationProdKeyRepository notiRepository;
    private final INotificationProdKeyFailedRepository failedNotiRepository;
    private final INotificationProdKeySuccess successNotiRepository;
    private final IOrderRepository orderRepository;
    private final ProductItemCommand productItemCommand;
    private final ObjectMapper objectMapper;

    @Transactional
    public void handleRetry1(long notiId, long orderId) {
        notiRepository.updateRetry1ById(notiId);
        orderRepository.updateStatusById(orderId, OrderStatus.RETRY_1.name());
    }

    @Transactional
    public void handleRetry2(long notiId, long orderId) {
        notiRepository.updateRetry2ById(notiId);
        orderRepository.updateStatusById(orderId, OrderStatus.RETRY_2.name());
    }

    @Transactional
    public void handleFailed(NotificationProdKey key) {
        NotificationProdKeyFail keyFailed = new NotificationProdKeyFail();
        keyFailed.setEmail(key.getEmail());
        keyFailed.setOrderId(key.getOrderId());
        keyFailed.setRetry1(key.getRetry1());
        keyFailed.setRetry2(key.getRetry2());
        long orderId = key.getOrderId();
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

            successNotiRepository.save(notiSuccess);
            productItemCommand.markItemUsed(flattenedProductItem_Ids);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void resendFailedNotification(Collection<Long> ids) {
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
