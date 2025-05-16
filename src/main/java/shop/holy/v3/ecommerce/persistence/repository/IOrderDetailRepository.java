package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.OrderDetail;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_OrderDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("""
            select odd.id, odd.productId, odd.orderId, odd.quantity,
                        p.name as productName, p.imageUrlId as imageUrlId
                        from OrderDetail odd left join Product p on odd.productId = p.id
                        where odd.orderId in (:orderId)""")
    List<ProQ_OrderDetails> findByOrderIdIn(Collection<Long> orderId);

    @EntityGraph(attributePaths = {"product"})
    List<OrderDetail> findAllByOrderId(long orderId);


    List<OrderDetail> findAllByOrderIdIn(Collection<Long> orderIds);

    @Query("""
            SELECT SUM(od.quantity) from
                        OrderDetail od join Order  o
                    where o.status =  :status
                                and o.createdAt >= :lowerBound AND
                            o.createdAt <= :upperBound
            """)
    long findSumQuantityByRecentTime(Date lowerBound, Date upperBound);
}
