package shop.holy.v3.ecommerce.persistence.repository.notification;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_NotificationProdKeyRow;

import java.util.List;

@Repository
public interface INotificationProdKeyRepository extends CrudRepository<NotificationProdKey, Long>, PagingAndSortingRepository<NotificationProdKey, Long> {

    @Query(value = """
            SELECT id, created_at, email, order_id, retry1, retry2
            FROM (SELECT *, ROW_NUMBER() OVER (PARTITION BY email ORDER BY id asc) AS rn
                  FROM notification_prod_keys npk) t
            WHERE rn = 1
            LIMIT 50
            """, nativeQuery = true)
    List<ProQ_NotificationProdKeyRow> findAllParititionByEmail(int limit);

    @Modifying
    @Query("UPDATE NotificationProdKey np SET np.retry1 = CURRENT_TIMESTAMP")
    int updateRetry1ById(long id);

    @Modifying
    @Query("UPDATE NotificationProdKey np SET np.retry2 = CURRENT_TIMESTAMP")
    int updateRetry2ById(long id);


}
