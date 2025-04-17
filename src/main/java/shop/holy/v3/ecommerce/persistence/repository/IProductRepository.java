package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.persistence.entity.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Modifying
    @Query("""
    update Product p set p.deletedAt = current_timestamp where p.id = :id
            """)
    int updateProductDeletedAt(Long id);


    @Modifying
    @Transactional
    @Query(value = "insert into product_favorites (profile_id, product_id) values (:accountId,:productId)", nativeQuery = true)
    int insertFavoriteProduct(Long accountId, Long productId);


    @Modifying
    @Transactional
    @Query(value = "delete from product_favorites where profile_id = :accountId and product_id = :productId", nativeQuery = true)
    int deleteFavoriteProduct(Long accountId, Long productId);


    @Query(value = """
            select p from  product_favorites fp
            join  products p on fp.product_id = p.id
            where fp.profile_id = :accountId
            """, nativeQuery = true)
    Page<Product> findFavorites(long accountId, Pageable pageable);

    List<Product> findAllByIdIn(Iterable<Long> ids);

    @Query(value = """
                            SELECT p FROM Product p
                            LEFT JOIN FETCH p.productDescription pd
                            LEFT JOIN FETCH p.variants pv
                            LEFT JOIN FETCH p.categories pc
                                WHERE p.id = :id
            """)
    @EntityGraph(attributePaths = {
            "productDescription",
            "categories",
            "variants"
    })
    Optional<Product> findByIdWithJoinFetch(long id);


    @EntityGraph(attributePaths = {
        "productDescription",
        "categories"
    })
    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL AND p.id = :id")
    Optional<Product> findFirstByIdEqualsAndDeletedAtIsNull(long id);

    @Modifying
    @Query(value = """
            INSERT INTO products_categories (product_id, category_id)
            VALUES (:productId, :categoryId)
            ON CONFLICT (product_id, category_id) DO NOTHING
            """, nativeQuery = true)
    int insertProductCategories(long productId, long categoryId);

    @Modifying
    @Query(value = """
            DELETE FROM products_categories
            WHERE product_id = :productId AND category_id = :categoryId
            """, nativeQuery = true)
    int deleteProductCategories(long productId, long categoryId);


}