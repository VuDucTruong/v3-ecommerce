package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Order;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @Modifying
    @Query("""
            update Order o set o.deletedAt = current_timestamp where o.id = :id
            """)
    int updateOrderDeletedAt(long id);

    @Modifying
    @Query("""
            update Order o set o.deletedAt = current_timestamp where o.id in (:ids)
            """)
    int updateOrderDeletedAtByIdIn(long[] ids);

    @Modifying
    @Query("UPDATE Order o set o.status = :status where o.id = :orderId")
    int updateOrderStatusById(String status, long orderId);

    Optional<Order> findFirstByIdEqualsAndDeletedAtIsNull(long id);

    Optional<Order> findFirstByIdEqualsAndProfileIdEqualsAndDeletedAtIsNull(long id, long profileId);

    Optional<Order> findFirstByIdEqualsAndProfileIdEquals(long id, long profileId);

    Optional<Order> findFirstByIdEquals(long id);

    @Query("SELECT o.amount from Order o where o.id = :orderId")
    Optional<BigDecimal> findAmountByOrderId(long orderId);


}
