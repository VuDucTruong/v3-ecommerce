package shop.holy.v3.ecommerce.persistence.repository.notification;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKeyFail;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface INotificationProdKeyFailedRepository extends CrudRepository<NotificationProdKeyFail, Long> {

    List<NotificationProdKeyFail> findAllByIdIn(Collection<Long> ids);

    @Query(value = """
            SELECT * from notification_prod_keys_fail npk where order_id = :orderId
                        LIMIT 1
            """, nativeQuery = true)
    Optional<String> findFirstReasonByOrderId(long orderId);

    boolean existsByOrderId(Long orderId);

}
