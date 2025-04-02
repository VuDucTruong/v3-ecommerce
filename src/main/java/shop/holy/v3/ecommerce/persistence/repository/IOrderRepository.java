package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Order;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
}
