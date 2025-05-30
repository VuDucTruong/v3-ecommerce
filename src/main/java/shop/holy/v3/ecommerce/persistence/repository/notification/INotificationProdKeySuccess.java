package shop.holy.v3.ecommerce.persistence.repository.notification;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;

import java.util.Optional;

@Repository
public interface INotificationProdKeySuccess extends CrudRepository<NotificationProdKey, Long>
{
    @Query("SELECT nps.mailProdKeyJson FROM NotificationProdKeySuccess nps where nps.orderId = :orderId")
    Optional<String> findFirstMailProdKeyJsonById(long orderId);

}
