package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.persistence.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            """ ,nativeQuery = true)
    Page<Product> findFavorites(long accountId, Pageable pageable);

    List<Product> findAllByIdIn(Iterable<Long> ids);

    Optional<Product> findFirstByIdEqualsAndDeletedAtIsNull(long id);


}
