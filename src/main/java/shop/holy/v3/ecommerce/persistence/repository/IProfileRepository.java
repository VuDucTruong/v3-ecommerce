package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Profile;

@Repository
public interface IProfileRepository extends JpaRepository<Profile, Long> {

    @Modifying
    @Query("""
            UPDATE Profile p
            SET p.fullName = :#{#profile.fullName},
                p.imageUrlId = :#{#profile.imageUrlId},
                p.phone = :#{#profile.phone}
            WHERE p.accountId = :#{#profile.accountId}
            """)
    void updateProfileById( Profile profile);

    @Modifying
    @Query("""
            UPDATE Profile p
            SET p.fullName = :#{#profile.fullName},
                p.phone = :#{#profile.phone}
            WHERE p.accountId = :#{#profile.accountId}
            """)
    void updateProfileExcludeImage(Profile profile);

    @Query("update Profile p set p.deletedAt = CURRENT_TIMESTAMP where p.accountId = :accountId")
    @Modifying
    int updateDeletedAtByAccountId(long accountId);

}
