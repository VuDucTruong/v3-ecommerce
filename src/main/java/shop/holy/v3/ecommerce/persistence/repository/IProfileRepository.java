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
                p.imageUrlId = :#{#profile.imageUrlId}
            WHERE p.accountId = :#{#profile.accountId}
            """)
    int updateProfileByAccountId(Profile profile);
    @Modifying
    @Query("""
            UPDATE Profile p
            SET p.fullName = :#{#profile.fullName},
                p.imageUrlId = :#{#profile.imageUrlId}
            WHERE p.id = :#{#profile.id}
            """)
    int updateProfileById(Profile profile);

    @Modifying
    @Query("""
            UPDATE Profile p
            SET p.fullName = :#{#profile.fullName}
            WHERE p.accountId = :#{#profile.accountId}
            """)
    int updateProfileExcludeImageByAccountId(Profile profile);

    @Modifying
    @Query("""
            UPDATE Profile p
            SET p.fullName = :#{#profile.fullName}
            WHERE p.id = :#{#profile.id}
            """)
    int updateProfileExcludeImageById(Profile profile);



    @Query("update Profile p set p.deletedAt = CURRENT_TIMESTAMP where p.accountId = :accountId")
    @Modifying
    int updateDeletedAtByAccountId(long accountId);


    @Query("update Profile p set p.deletedAt = current_timestamp where p.accountId in :accountIds")
    @Modifying
    int updateDeletedAtByAccountIdIn(long[] accountIds);

}
