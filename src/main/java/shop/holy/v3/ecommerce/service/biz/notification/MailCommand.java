package shop.holy.v3.ecommerce.service.biz.notification;

import jakarta.mail.MessagingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_Email_Fullname;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_OrderDetails;
import shop.holy.v3.ecommerce.persistence.repository.IAccountRepository;
import shop.holy.v3.ecommerce.persistence.repository.IOrderDetailRepository;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductItemRepository;
import shop.holy.v3.ecommerce.service.smtp.SmtpService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@Slf4j
//@DependsOn({"org.springframework.boot.autoconfigure.task."})
public class MailCommand {
    private final IOrderDetailRepository orderDetailRepository;
    private final IAccountRepository accountRepository;

    private final NotificationCommand notificationCommand;
    private final Executor executor;

    private final SmtpService smtpService;
    private final IProductItemRepository productItemRepository;

    public MailCommand(IOrderDetailRepository orderDetailRepository, IAccountRepository accountRepository,
                       NotificationCommand notificationCommand, @Qualifier("applicationTaskExecutor") Executor executor,
                       SmtpService smtpService, IProductItemRepository productItemRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.accountRepository = accountRepository;
        this.notificationCommand = notificationCommand;
        this.executor = executor;
        this.smtpService = smtpService;
        this.productItemRepository = productItemRepository;
    }


    public void resolveNotificationProdKey(List<NotificationProdKey> notis) {
        Map<Long, UserPack> orderId_pack = queryFullnamesAndMapToNotifications(notis);
        var details = queryOrderDetails(notis);
        Map<Long, Map<Long, MailProductKeys.ProductMeta>> orderId_To_metaMap = resolveOrderDetailsToMap(details);

//        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (var entry : orderId_To_metaMap.entrySet()) {
            /// set metadata
            UserPack userPack = orderId_pack.get(entry.getKey());
            ///  WITH FIRM HOPE THAT IT WILL TRIGGER
            fetchAndResolveMail(userPack, entry);
        }
    }

    private CompletableFuture<Void> fetchAndResolveMail(UserPack userPack, Map.Entry<Long, Map<Long, MailProductKeys.ProductMeta>> orderId_to_MetaMapEntry) {
        CompletableFuture<Pair<NotificationProdKey, MailProductKeys>> futurePair_Noti_Mail = CompletableFuture.supplyAsync(() -> {
            NotificationProdKey noti = userPack.getNotificationProdKey();

            MailProductKeys mpk = new MailProductKeys();
            {
                mpk.setEmail(noti.getEmail());
                mpk.setFullName(userPack.getFullName());
                mpk.setOrderId(orderId_to_MetaMapEntry.getKey());
                mpk.setMetas(orderId_to_MetaMapEntry.getValue());
            }

            for (var metaEntry : mpk.getMetas().entrySet()) {
                var meta = metaEntry.getValue();
                var productItems = productItemRepository.findAllByProductId(metaEntry.getKey(),
                        PageRequest.of(0, meta.getQuantity(), Sort.Direction.ASC, "id"));
                productItems.forEach(item -> {
                    meta.getKeys().add(new MailProductKeys.ProductKey(item.getId(), item.getProductKey(), "1 month"));
                });
            }
            return Pair.of(noti, mpk);
        }, executor);

        return futurePair_Noti_Mail.thenAcceptAsync(pair -> {
            var noti = pair.getLeft();
            var mpk = pair.getRight();
            try {
                smtpService.sendMailProductKeys(mpk);
                notificationCommand.handleSuccess(noti, mpk);
            } catch (MessagingException e) {
                if (noti.getRetry1() == null)
                    notificationCommand.handleRetry1(noti.getId(), noti.getOrderId());
//                else if (noti.getRetry2() == null)
//                    notificationCommand.handleRetry2(noti.getId(), noti.getOrderId());
                else {
                    notificationCommand.handleFailed(noti);
                }
                /// NOT INSERT, SO THAT WE GONNA RESOLVE IN NEXT CYCLE
                log.error("Scheduler smtp error ", e);
            }
        }, executor);
    }

    private Map<Long, Map<Long, MailProductKeys.ProductMeta>> resolveOrderDetailsToMap(Collection<ProQ_OrderDetails> details) {
        Map<Long, Map<Long, MailProductKeys.ProductMeta>> orderId_By_metaMap = new HashMap<>();
        for (ProQ_OrderDetails detail : details) {
            var nameByKeys = orderId_By_metaMap.computeIfAbsent(detail.getOrderId(), _ -> new HashMap<>());
            MailProductKeys.ProductMeta meta = nameByKeys.get(detail.getProductId());
            if (meta == null) {
                meta = new MailProductKeys.ProductMeta();
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
                Collectors.toMap(NotificationProdKey::getOrderId, UserPack::new));

        List<String> emails = notis.stream().map(NotificationProdKey::getEmail).toList();
        var mail_fullName = accountRepository.findAllProQEmailFullname(emails).stream().collect(
                Collectors.toMap(ProQ_Email_Fullname::getEmail, ProQ_Email_Fullname::getFullName));
        orderId_pack.forEach((_, value) -> {
            String fullName = mail_fullName.getOrDefault(value.getNotificationProdKey().getEmail(), "Traveler ");
            value.setFullName(fullName);
        });
        return orderId_pack;
    }


    @Getter
    @Setter
    @RequiredArgsConstructor
    public static class UserPack {
        final NotificationProdKey notificationProdKey;
        String fullName;
    }
}
