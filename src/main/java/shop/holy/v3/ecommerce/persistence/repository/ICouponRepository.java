package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Coupon;

import java.util.Optional;

@Repository
public interface ICouponRepository extends JpaRepository<Coupon, Long>, JpaSpecificationExecutor<Coupon> {

    @Modifying
    @Query("DELETE FROM Coupon c WHERE c.id = :id")
    int deleteCouponById(long id);

    Optional<Coupon> findFirstByCode(String code);
    Optional<Coupon> findFirstByIdAndDeletedAtIsNull(long id);
    Optional<Coupon> findFirstByCodeAndDeletedAtIsNull(String code);

    @Query(value = """
            update coupons c set current_usage = current_usage + 1
                             where c.code = :code and c.current_usage < c.usage_limit
            and c.deleted_at is null
            and c.available_from <= now() and c.available_to >= now()
            returning *
            """,nativeQuery = true)
    Optional<Coupon> updateCouponUsage(String code);

    @Modifying
    @Query("UPDATE Coupon c SET c.deletedAt = current_timestamp WHERE c.id = :id")
    int updateDeletedAtById(long id);
}
