package shop.holy.v3.ecommerce.persistence.repository.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Account;

import java.util.Optional;

@Repository
public interface ITestRepository extends JpaRepository<Account, Long> {

    Optional<Account> findFirstByRoleEquals(String role);
}
