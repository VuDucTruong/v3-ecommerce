package shop.holy.v3.ecommerce.persistence.repository;

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
//    List<NotificationProdKey> findAll


}
