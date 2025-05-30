package shop.holy.v3.ecommerce.service.smtp;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;
import shop.holy.v3.ecommerce.service.biz.notification.MailCommand;
import shop.holy.v3.ecommerce.service.biz.notification.NotificationQuery;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledSmtp {
    private final NotificationQuery notificationQuery;
    private final MailCommand mailCommand;

    @Async
    @Profile({"!kafka","mail"})
    @Scheduled(cron = "${phong.mail.scheduled.cron}")
    protected void runAsyncTask() {
        List<NotificationProdKey> notis = notificationQuery.findFirstAtEachMailPartition(50);
        mailCommand.resolveNotificationProdKey(notis);
    }

}
