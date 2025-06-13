package shop.holy.v3.ecommerce.persistence.repository.product;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.persistence.entity.product.Product;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_TotalSold_Product;

import java.util.*;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // Add this method to override the default findAll
    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"productDescription", "categories", "tags"})
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @Query(value = "select id from products where id IN (:ids)", nativeQuery = true)
    Set<Long> findExistingProductIds(Set<Long> ids);

    @Query(value = "select id from products where id IN (:ids) AND deleted_at is null", nativeQuery = true)
    Set<Long> findExistingProductIdsAndDeletedAtIsNull(Set<Long> ids);

    @Modifying
    @Query("""
            update Product p set p.deletedAt = current_timestamp where p.id = :id
            """)
    int updateProductDeletedAt(Long id);

    @Modifying
    @Query("""
            update Product p set p.deletedAt = current_timestamp where p.id in (:ids)
            """)
    int updateProductDeletedAtByIdIn(long[] ids);

    List<Product> findProductsByIdIn(Collection<Long> id);


    @Modifying
    @Query(value = """
            UPDATE products p set 
            slug = coalesce(:#{#product.slug}, p.slug),
                        name = :#{#product.name},
                        image_url_id = coalesce(:#{#product.imageUrlId}, p.image_url_id),
                        original_price = :#{#product.originalPrice},
                        price = :#{#product.price},
                        prod_desc_id = :#{#product.proDescId},
                        group_id = :#{#product.groupId},
                        represent = :#{#product.represent}
            where p.id = :#{#product.id}
            """, nativeQuery = true)
    int updateProductById(Product product);


    @Query(value = """
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.productDescription pd
            LEFT JOIN FETCH p.categories pc
            LEFT JOIN FETCH p.tags
                WHERE p.id = :id
            """)
    Optional<Product> findByIdWithJoinFetch(long id);


    @Query(value = """
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.productDescription pd
            LEFT JOIN FETCH p.categories pc
            LEFT JOIN FETCH p.tags
                WHERE p.id = :id AND p.deletedAt is null
            """)
    Optional<Product> findFirstByIdEqualsAndDeletedAtIsNull(long id);

    @Query(value = """
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.productDescription pd
            LEFT JOIN FETCH p.categories pc
            LEFT JOIN FETCH p.tags
                WHERE p.slug = :slug AND p.deletedAt is null
            """)
    Optional<Product> findFirstBySlugEqualsAndDeletedAtIsNull(String slug);

    @Query(value = """
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.productDescription pd
            LEFT JOIN FETCH p.categories pc
            LEFT JOIN FETCH p.tags
                WHERE p.slug = :slug AND p.deletedAt is null
            """)
    Optional<Product> findFirstBySlug(String slug);

    @Modifying
    @Query(value = """
            INSERT INTO products_categories (product_id, category_id)
            VALUES (:productId, :categoryId)
            ON CONFLICT (product_id, category_id) DO NOTHING
            """, nativeQuery = true)
    void insertProductCategories(long productId, long categoryId);

    @Modifying
    @Query(value = """
            DELETE FROM products_categories
            WHERE product_id = :productId AND category_id = :categoryId
            """, nativeQuery = true)
    void deleteProductCategories(long productId, long categoryId);

    Set<Product> findByGroupId(Long groupId);

    Set<Product> findByGroupIdAndDeletedAtIsNull(Long groupId);


    @Query(value = "select category_id from products_categories where product_id = ?1", nativeQuery = true)
    Set<Long> findCategoryIdsByProductId(long productId);


    @Modifying
    @Query("UPDATE Product p set p.quantity = p.quantity + :subTract where p.id = :productId")
    void updateAddProductItemCountsByProductIdEquals(long productId, long subTract);

    @Query(value = """
            SELECT p.*, totalSold FROM (SELECT od.product_id, SUM(od.quantity) AS totalSold
                FROM order_details od
                GROUP BY od.product_id
                ORDER BY totalSold DESC
                LIMIT :limit) t
                RIGHT JOIN products p ON t.product_id = p.id
            ORDER BY t.totalSold IS NULL, t.totalSold DESC
            LIMIT :limit;
            """, nativeQuery = true)
    List<ProQ_TotalSold_Product> findProductSortBySumQuantity(int limit);
}