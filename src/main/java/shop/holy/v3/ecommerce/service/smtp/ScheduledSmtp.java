package shop.holy.v3.ecommerce.service.smtp;


import jakarta.mail.MessagingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_Email_Fullname;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_OrderDetails;
import shop.holy.v3.ecommerce.persistence.repository.*;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductItemRepository;

import java.util.*;
import java.util.stream.Collectors;

import static shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys.*;

@Component
@RequiredArgsConstructor
public class ScheduledSmtp {
    private final IOrderDetailRepository orderDetailRepository;
    private final IAccountRepository accountRepository;
    private final INotificationProdKeyRepository notiRepository;

    private final SmtpService smtpService;
    private final IProductItemRepository productItemRepository;
    private final Pageable pageable = PageRequest.of(0, 50, Sort.Direction.ASC, "id");


    @Async
    @Scheduled(cron = "${phong.mail.scheduled.cron}")
    public void runAsyncTask() {
        List<NotificationProdKey> notis = notiRepository.findAllParititionByEmail(50);

        Map<Long, UserPack> orderId_pack = queryFullnamesAndMapToNotifications(notis);
        var details = queryOrderDetails(notis);
        Map<Long, Map<Long, ProductMeta>> orderId_By_metaMap = resolveOrderDetailsToMap(details);

        for (var entry : orderId_By_metaMap.entrySet()) {
            MailProductKeys mpk = new MailProductKeys();
            /// set metadata
            {
                mpk.setEmail(orderId_pack.get(entry.getKey()).getEmail());
                mpk.setFullName(orderId_pack.get(entry.getKey()).getFullName());
                mpk.setOrderId(entry.getKey());
            }

            mpk.setMetas(orderId_By_metaMap.get(entry.getKey()));
            for (var metaEntry : mpk.getMetas().entrySet()) {
                var meta = metaEntry.getValue();
                var productItems = productItemRepository.findAllByProductId(metaEntry.getKey(),
                        PageRequest.of(0, meta.getQuantity(), Sort.Direction.ASC, "id"));
                productItems.forEach(item -> {
                    meta.getKeys().add(new ProductKey(item.getProductKey(), "1 month"));
                });
            }

            try {
                smtpService.sendMailProductKeys(mpk);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private Map<Long, Map<Long, ProductMeta>> resolveOrderDetailsToMap(Collection<ProQ_OrderDetails> details) {
        Map<Long, Map<Long, ProductMeta>> orderId_By_metaMap = new HashMap<>();
        for (ProQ_OrderDetails detail : details) {
            var nameByKeys = orderId_By_metaMap.computeIfAbsent(detail.getOrderId(), _ -> new HashMap<>());
            ProductMeta meta = nameByKeys.get(detail.getProductId());
            if (meta == null) {
                meta = new ProductMeta();
                meta.setQuantity(detail.getQuantity());
                meta.setProductName(detail.getProductName());
                nameByKeys.put(detail.getProductId(), meta);
            } else
                meta.addQty(detail.getQuantity());
        }
        return orderId_By_metaMap;
    }

    private List<ProQ_OrderDetails> queryOrderDetails(List<NotificationProdKey> notis) {
        List<Long> orderIds = notis.stream().map(NotificationProdKey::getOrderId).collect(Collectors.toList());
        return orderDetailRepository.findByOrderIdIn(orderIds);
    }

    private Map<Long, UserPack> queryFullnamesAndMapToNotifications(List<NotificationProdKey> notis) {
        Map<Long, UserPack> orderId_pack = notis.stream().collect(
                Collectors.toMap(NotificationProdKey::getOrderId, s -> new UserPack(s.getEmail())));

        List<String> emails = notis.stream().map(NotificationProdKey::getEmail).toList();
        var mail_fullName = accountRepository.findAllProQEmailFullname(emails).stream().collect(
                Collectors.toMap(ProQ_Email_Fullname::getEmail, ProQ_Email_Fullname::getFullName));
        orderId_pack.forEach((_, value) -> {
            String fullName = mail_fullName.getOrDefault(value.getEmail(), "Traveler ");
            value.setFullName(fullName);
        });
        return orderId_pack;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class UserPack {
        final String email;
        String fullName;
    }


}
