package shop.holy.v3.ecommerce.persistence.repository;

import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Account;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_Email_Fullname;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_Email_Otp_OtpExpiry;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    @EntityGraph(attributePaths = {"profile"})
    Account findByEmail(@Size(max = 100) String email);


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
            WHERE id = :id
            """, nativeQuery = true)
    int changePassword(long id, @Param("password") String password);


    @Modifying
    @Query(value = """
            UPDATE accounts
            SET is_verified = true, otp = NULL, otp_expiry = NULL
                        where id = :id
            """, nativeQuery = true)
    int verifyMail(long id);


    Optional<Account> findByIdAndDeletedAtIsNull(long id);

    @Modifying
    @Query(value = """
                update accounts set deleted_at = now()
                where id = :id
            """, nativeQuery = true)
    int updateDeletedAtById(long id);

    @Query("update Account a set a.deletedAt = current_timestamp where a.id in :ids")
    @Modifying
    int updateDeletedAtByIdIn(long[] ids);

    @Query("select a.email as email, p.fullName as fullName from Account a join Profile p on a.id = p.accountId where email in (:emails)")
    List<ProQ_Email_Fullname> findAllProQEmailFullname(Collection<String> emails);

    int countAccountByRoleAndCreatedAtAfterAndCreatedAtBefore(String role, Date lowerBound, Date upperBound);
}
