package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Order;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_Date_Revenue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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

    Optional<Order> findFirstByIdEqualsAndDeletedAtIsNull(long id);

    Optional<Order> findFirstByIdEqualsAndProfileIdEqualsAndDeletedAtIsNull(long id, long profileId);

    @Query("SELECT o.amount from Order o where o.id = :orderId")
    Optional<BigDecimal> findAmountByOrderId(long orderId);

    @Query("""
            select SUM(o.amount) from Order o
                        where o.status =  :status
                        and o.createdAt >= :lowerBound AND
                            o.createdAt <= :upperBound
            """)
    Optional<BigDecimal> findSumTotalByRecentTime(String status, Date lowerBound, Date upperBound);

    @EntityGraph(attributePaths = {"payment"})
    List<Order> findAllByCreatedAtBeforeAndCreatedAtAfter(Date createdAtBefore, Date createdAtAfter);

    long countByStatusAndCreatedAtLessThanAndCreatedAtGreaterThan(String status, Date lowerBound, Date upperBound);

    @Query("""
                select cast(o.createdAt as date) as date, sum(o.amount) as revenue
                from Order o
                where o.status = :status
                  and o.createdAt >= :lowerBound
                  and o.createdAt <= :upperBound
                group by cast(o.createdAt as date)
                order by cast(o.createdAt as date)
            """)
    List<ProQ_Date_Revenue> findRevenues(String status, Date lowerBound, Date upperBound);

    @Modifying
    @Query("UPDATE Order o set o.status = :status where o.id = :id")
    int updateStatusById(long id, String status);
}
