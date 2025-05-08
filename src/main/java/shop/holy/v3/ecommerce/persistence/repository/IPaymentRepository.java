package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Payment;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_PayUrl_Status;

import java.util.Optional;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment, Long> {

    @EntityGraph(attributePaths = {"order"})
    Optional<Payment> findFirstByOrderIdAndStatusEquals(long orderId, String status);

    Optional<ProQ_PayUrl_Status> findFirstPaymentUrlAndStatusByOrderId(long orderId);

    @Modifying
    @Query("""
                UPDATE Payment p set
                p.status = :#{#p2.status}, p.paymentMethod = :#{#p2.paymentMethod},
                p.bankCode = :#{#p2.bankCode},
                p.detailCode = :#{#p2.detailCode}, p.detailMessage = :#{#p2.detailMessage},
                p.note = :#{#p2.note}, p.cardType = :#{#p2.cardType}
            WHERE
            p.transRef = :#{#p2.transRef} AND p.secureHash = :#{#p2.secureHash}
            """)
    int updatePaymentByTransRefAndSecureHash(Payment p2);




}
