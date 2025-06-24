package shop.holy.v3.ecommerce.service.smtp;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;
import shop.holy.v3.ecommerce.service.biz.notification.MailCommand;
import shop.holy.v3.ecommerce.service.biz.notification.NotificationQuery;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"mail & !kafka"})
@EnableScheduling
public class ScheduledSmtp {
    private final NotificationQuery notificationQuery;
    private final MailCommand mailCommand;

    @Async
    @Scheduled(cron = "${phong.mail.scheduled.cron}")
    public void runAsyncTask() {
        log.info("Starting scheduled async task");
        List<NotificationProdKey> notis = notificationQuery.findFirstAtEachMailPartition(50);
        if (CollectionUtils.isEmpty(notis))
            return;
        mailCommand.resolveNotificationProdKey(notis);
        log.info("Finished async task");
    }


}
