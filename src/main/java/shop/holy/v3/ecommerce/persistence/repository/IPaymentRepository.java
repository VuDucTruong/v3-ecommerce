package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Payment;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_PayUrl_Status;

import java.util.Optional;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment, Long> {

    @EntityGraph(attributePaths = {"order"})
    Optional<Payment> findFirstByOrderIdAndStatusEquals(long orderId, String status);

    Optional<ProQ_PayUrl_Status> findFirstPaymentUrlAndStatusAndExpiryByOrderId(long orderId);

    @Query(value = """
                UPDATE payments p set
                status = :#{#p2.status}, payment_method = :#{#p2.paymentMethod},
                bank_code = :#{#p2.bankCode},
                detail_code = :#{#p2.detailCode}, detail_message = :#{#p2.detailMessage},
                note = :#{#p2.note}, card_type = :#{#p2.cardType}
            WHERE
                trans_ref = :#{#p2.transRef}
                returning order_id
            """, nativeQuery = true)
    Optional<Long> updatePaymentByTransRef(Payment p2);




}
