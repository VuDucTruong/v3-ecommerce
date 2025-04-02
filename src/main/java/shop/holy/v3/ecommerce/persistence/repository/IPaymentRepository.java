package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Payment;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment, Long> {
}
