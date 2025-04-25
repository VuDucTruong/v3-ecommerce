package shop.holy.v3.ecommerce.persistence.repository;

import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Account;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    @EntityGraph(attributePaths = {"profile.id"})
    Account findByEmail(@Size(max = 100) String email);

    @Modifying
    @Query(value = """
            update accounts set otp = :otp, otp_expiry = now() + interval '15 minutes'
                            where email = :email
            """, nativeQuery = true)
    int saveOtp(String email, String otp);


    @Modifying
    @Query(value = """
            update accounts set password = :password
                            where otp = :otp and otp_expiry > now()
            """, nativeQuery = true)
    int changePassword(String otp, String password);

    Optional<Account> findByIdAndDeletedAtIsNull(long id);

    @Modifying
    @Query(value = """
                update accounts set deleted_at = now()
                where id = :id
            """, nativeQuery = true)
    int updateDeletedAtById(long id);

}
