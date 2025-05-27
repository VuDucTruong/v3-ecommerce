package shop.holy.v3.ecommerce.persistence.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Product;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // Add this method to override the default findAll
    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"productDescription", "categories"})
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

    @Query(value = "select id from products where id IN (:ids) and created_at is not null", nativeQuery = true)
    Set<Long> findExistingProductIds(Set<Long> ids);

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
                        represent = :#{#product.represent},
                        tags = CAST(:tags AS jsonb)
            where p.id = :#{#product.id}
            """, nativeQuery = true)
    int updateProductById(Product product, String tags);



    @Query(value = """
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.productDescription pd
            LEFT JOIN FETCH p.group pg
            LEFT JOIN FETCH pg.variants
            LEFT JOIN FETCH p.categories pc
                WHERE p.id = :id
            """)
    Optional<Product> findByIdWithJoinFetch(long id);


    @Query(value = """
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.productDescription pd
            LEFT JOIN FETCH p.group pg
            LEFT JOIN FETCH pg.variants
            LEFT JOIN FETCH p.categories pc
                WHERE p.id = :id AND p.deletedAt is null
            """)
    Optional<Product> findFirstByIdEqualsAndDeletedAtIsNull(long id);

    @Query(value = """
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.productDescription pd
            LEFT JOIN FETCH p.group pg
            LEFT JOIN FETCH pg.variants
            LEFT JOIN FETCH p.categories pc
                WHERE p.slug = :slug AND p.deletedAt is null
            """)
    Optional<Product> findFirstBySlugEqualsAndDeletedAtIsNull(String slug);

    @Query(value = """
            SELECT DISTINCT p FROM Product p
            LEFT JOIN FETCH p.productDescription pd
            LEFT JOIN FETCH p.group pg
            LEFT JOIN FETCH pg.variants
            LEFT JOIN FETCH p.categories pc
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

    @Query(value = "select category_id from products_categories where product_id = ?1", nativeQuery = true)
    Set<Long> findCategoryIdsByProductId(long productId);


    @Modifying
    @Query("UPDATE Product p set p.quantity = p.quantity + :subTract where p.id = :productId")
    void updateAddProductItemCountsByProductIdEquals(long productId, long subTract);

}