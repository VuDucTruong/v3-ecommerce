package shop.holy.v3.ecommerce.service.biz.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;
import shop.holy.v3.ecommerce.persistence.repository.notification.INotificationProdKeyRepository;
import shop.holy.v3.ecommerce.persistence.repository.notification.INotificationProdKeySuccess;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationQuery {
    private final INotificationProdKeySuccess successNotiRepository;
    private final INotificationProdKeyRepository notiRepository;
    private final ObjectMapper objectMapper;

    public Iterable<NotificationProdKey> getNotificationProdKeys() {
        return notiRepository.findAll();
    }

    public MailProductKeys getSentMailMetaByOrderIdAndStatus(long orderId, String status) {
        if (!OrderStatus.COMPLETED.name().equals(status))
            return null;

        return successNotiRepository.findFirstMailProdKeyJsonById(orderId)
                .map(json -> {
                    try {
                        return objectMapper.readValue(json, MailProductKeys.class);
                    } catch (JsonProcessingException e) {
                        return null;
                    }
                })
                .orElse(null);
    }

    public List<NotificationProdKey> findFirstAtEachMailPartition(int limit) {
        var entities = notiRepository.findAllParititionByEmail(limit).stream().map(row -> {
            NotificationProdKey key = new NotificationProdKey();
            key.setId(row.id());
            key.setOrderId(row.orderId());
            key.setEmail(row.email());
            key.setRetry1(row.retry1());
            key.setRetry2(row.retry2());
            return key;
        }).toList();

        return entities;
    }

}
