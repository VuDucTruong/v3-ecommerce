package shop.holy.v3.ecommerce.persistence.repository;

import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Account;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    @EntityGraph(attributePaths = {"profile"})
    Account findByEmail(@Size(max = 100) String email);

    @Query("SELECT 1 from Account a where a.email = :email")
    Optional<Integer> isEmailExist(String email);

    @Modifying
    @Query(value = """
            update accounts set otp = :otp, otp_expiry = now() + interval '15 minutes'
                            where email = :email
            """, nativeQuery = true)
    int saveOtp(String email, String otp);


    @Modifying
    @Query(value = """
            UPDATE accounts
            SET password = :password, otp = NULL, otp_expiry = NULL
            WHERE email = :email
              AND otp IS NOT NULL
              AND otp = :otp
                AND otp_expiry is not null
              AND otp_expiry > NOW()
            """, nativeQuery = true)
    int changePassword(@Param("otp") String otp,
                       @Param("email") String email,
                       @Param("password") String password);

    Optional<Account> findByIdAndDeletedAtIsNull(long id);

    @Modifying
    @Query(value = """
                update accounts set deleted_at = now()
                where id = :id
            """, nativeQuery = true)
    int updateDeletedAtById(long id);

    @Query("update Account a set a.deletedAt = current_timestamp where a.id in :ids")
    @Modifying
    int updateDeletedAtByIdIn(long[]ids);
}
