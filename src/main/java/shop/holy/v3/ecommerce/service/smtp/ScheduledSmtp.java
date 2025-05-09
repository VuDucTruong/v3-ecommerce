package shop.holy.v3.ecommerce.service.smtp;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shop.holy.v3.ecommerce.persistence.repository.INotificationProdKeyRepository;
import shop.holy.v3.ecommerce.persistence.repository.IOrderDetailRepository;

@Component
@RequiredArgsConstructor
public class ScheduledSmtp {
    private final IOrderDetailRepository orderDetailRepository;
    private final INotificationProdKeyRepository notiRepository;

    @Async
    @Scheduled(cron = "${phong.mail.scheduled.cron}")
    public void runAsyncTask() {

    }

}
