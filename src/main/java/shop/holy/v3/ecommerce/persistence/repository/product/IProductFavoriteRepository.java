package shop.holy.v3.ecommerce.persistence.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.persistence.entity.product.Product;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductFavorite;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IProductFavoriteRepository extends JpaRepository<ProductFavorite, Long> {

    @Modifying
    @Transactional
    @Query(value = "insert into product_favorites (profile_id, product_id) values (:accountId,:productId) ON CONFLICT (profile_id,product_id) DO NOTHING", nativeQuery = true)
    void insertFavoriteProduct(Long accountId, Long productId);

    @Modifying
    @Transactional
    @Query(value = "delete from product_favorites where profile_id = :accountId and product_id = :productId", nativeQuery = true)
    void deleteFavoriteProduct(Long accountId, Long productId);

    @Query(value = """
            select p.* from products p join product_favorites fp
                        on p.id = fp.product_id
                        where fp.profile_id = :profileId
            """, nativeQuery = true)
    Page<Product> findFavorites(long profileId, Pageable pageable);

    @Query(value = "SELECT * from product_favorites pf where pf.profile_id =:profileId " ,nativeQuery = true)
    List<Long> findAllIdsByAccountId(Long profileId);

    @Query(value = """
            SELECT 1 from product_favorites fp
                        where product_id = :productId
                        AND profile_id = :profileId
            """, nativeQuery = true)
    Optional<Integer> checkFavorite(long profileId, long productId);



    @Query(value = "select product_id from product_favorites where profile_id=:profileId AND product_id IN (:productIds)", nativeQuery = true)
    Set<Long> checkFavorites(long profileId, Collection<Long> productIds);


}
