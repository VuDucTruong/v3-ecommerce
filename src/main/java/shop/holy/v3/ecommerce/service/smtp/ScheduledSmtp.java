package shop.holy.v3.ecommerce.service.smtp;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys;
import shop.holy.v3.ecommerce.persistence.entity.OrderDetail;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_Email_Fullname;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_OrderDetails;
import shop.holy.v3.ecommerce.persistence.repository.*;

import java.util.*;
import java.util.stream.Collectors;

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
        Page<NotificationProdKey> notis = notiRepository.findAll(pageable);

        Map<Long, String> orderId_emails = notis.stream().collect(Collectors.toMap(NotificationProdKey::getOrderId, NotificationProdKey::getEmail));

        Set<Long> orderIds = orderId_emails.keySet();
        List<String> emails = orderId_emails.values().stream().toList();

        var orderDetails = orderDetailRepository.findAllByOrderIdIn(orderIds);
        List<ProQ_Email_Fullname> emailFullnames = accountRepository.findAllProQEmailFullname(emails);
        Map<String,String> email_fullNamesMap = emailFullnames.stream().collect(Collectors.toMap(ProQ_Email_Fullname::getEmail, ProQ_Email_Fullname::getFullName));
        List<ProQ_OrderDetails> details = orderDetailRepository.findByOrderIdIn(orderIds);

        List<Pair<String,Long>> email_ProductIdKeyss = new ArrayList<>();
        for (ProQ_OrderDetails detail : details) {
            String email = orderId_emails.get(detail.getOrderId());
            String fullName = email_fullNamesMap.get(email);

        }


        List<Pair<String, Long>> email_prodIds = new ArrayList<>();

        ///  email_ProductKeys_name

        var test = notis.stream().map(n -> {
            var temp = new Packphase1();
            return new AbstractMap.SimpleEntry<>(n.getEmail(), temp);
        }).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (v1, v2) -> {
                    v1.getOrderId().addAll(v2.getOrderId());
                    return v1;
                }
        ));

        var x = orderDetails.stream().map(od -> Map.entry(od.getProductId(), od.getQuantity())).toList();
        /// 1 (email, productId, quantity)



    }

    @Getter
    @Setter
    public static class Packphase1 {
        Set<Long> orderId;
    }

    public static class Packphase2 {
        String email;
        long productId;
        String name;
        String productName;
        long quantity;
    }

}
