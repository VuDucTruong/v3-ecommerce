package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Category;

import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    Optional<Category> findFirstByIdAndDeletedAtIsNull(Long id);

    @Modifying
    @Query(value = "DELETE FROM products_categories where category_id=:categoryId", nativeQuery = true)
    void deleteProductCategoryByCategoryIdEquals(long categoryId);

    @Modifying
    @Query(value = "DELETE FROM products_categories where category_id = ANY(:categoryIds)",nativeQuery = true)
    void deleteProductCategoryByCategoryIdIn(long[] categoryIds);

    @Modifying
    @Query("UPDATE Category c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.id = :id")
    int updateDeletedAtById(Long id);

    @Modifying
    @Query("UPDATE Category c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.id in (:ids)")
    int updateDeletedAtByIdIn(long[] ids);

    int deleteAllByIdIn(long[] ids);

    int deleteByIdEquals(long id);

}
