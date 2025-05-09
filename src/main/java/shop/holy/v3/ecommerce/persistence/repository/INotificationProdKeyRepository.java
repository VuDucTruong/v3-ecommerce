package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;

import java.util.List;

@Repository
public interface INotificationProdKeyRepository extends CrudRepository<NotificationProdKey, Long> {

//    @Query(value = """
//DELETE FROM
//""", nativeQuery = true)
//    List<NotificationProdKey> deleteAllWithReturnings();

}
