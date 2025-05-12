package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.notification.NotificationProdKey;

@Repository
public interface INotificationProdKeyRepository extends CrudRepository<NotificationProdKey, Long>, PagingAndSortingRepository<NotificationProdKey, Long> {


//    List<NotificationProdKey> findAll


}
