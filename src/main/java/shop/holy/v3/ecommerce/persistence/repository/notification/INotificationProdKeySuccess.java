package shop.holy.v3.ecommerce.persistence.repository.notification;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;

import java.util.Optional;

@Repository
public interface INotificationProdKeySuccess extends CrudRepository<NotificationProdKey, Long> {

    @Query(value = """
            SELECT npk.mail_prod_key_json from notification_prod_keys_success npk where npk.order_id = :orderId
                        order by created_at desc LIMIT 1
            """, nativeQuery = true)
    Optional<String> findFirstMailProdKeyJsonById(long orderId);

}
