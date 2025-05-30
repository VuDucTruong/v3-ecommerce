package shop.holy.v3.ecommerce.persistence.repository.notification;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;

import java.util.List;

@Repository
public interface INotificationProdKeyRepository extends CrudRepository<NotificationProdKey, Long>, PagingAndSortingRepository<NotificationProdKey, Long> {

    @Query(value = """
            SELECT * FROM (
                SELECT *, ROW_NUMBER() OVER (PARTITION BY email ORDER BY id asc) AS rn
                FROM notification_prod_keys
            ) t
            WHERE rn = 1 LIMIT :limit
            """, nativeQuery = true)
    List<NotificationProdKey> findAllParititionByEmail(int limit);

    @Modifying
    @Query("UPDATE NotificationProdKey np SET np.retry1 = CURRENT_TIMESTAMP")
    int updateRetry1ById(long id);

    @Modifying
    @Query("UPDATE NotificationProdKey np SET np.retry2 = CURRENT_TIMESTAMP")
    int updateRetry2ById(long id);



}
