package shop.holy.v3.ecommerce.persistence.repository.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductItem;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_Id_ProductId_AcceptedKey;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductId_Quantity;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductItemLight;

import java.util.List;
import java.util.Optional;

@Repository
@SuppressWarnings({"Update"})
public interface IProductItemRepository extends JpaRepository<ProductItem, Long>, JpaSpecificationExecutor<ProductItem> {


    List<ProductItem> findAllByProductId(long productId, Pageable pageable);

    Optional<ProQ_ProductItemLight> findFirstById(long id);

    List<ProQ_ProductItemLight> findAllByProductIdEquals(long productId);

    //    @EntityGraph(attributePaths = {"product"})
    Optional<ProQ_ProductItemLight> findFirstByProductKey(String productKey);


    @Modifying
    @Query("""
            UPDATE ProductItem pi set pi.productKey = :#{#productItem.productKey},
            pi.productId = :#{#productItem.productId},
            pi.region = :#{#productItem.region},
            pi.account = :#{#productItem.account}
            WHERE pi.id = :#{#productItem.id}
            """)
    int updateProductItemById(@Param("productItem") ProductItem productItem);


    @Query(value = """
            INSERT INTO product_items (product_id, product_key, region, account)
            (SELECT id, key, region, account::jsonb
            FROM unnest(:productIds, :productKeys, :regions, :accounts) as t(id, key, region,account))
            ON CONFLICT (product_id,product_key) DO NOTHING
            RETURNING id, product_key AS accepted_key, product_id
            """, nativeQuery = true)
    List<ProQ_Id_ProductId_AcceptedKey> insertProductItems(
            @Param("productIds") long[] productIds,
            @Param("productKeys") String[] productKeys,
            @Param("accounts") String[] accounts,
            @Param("regions") String[] regions
            );


    @Query(value = """
            WITH deleted AS (
                DELETE FROM product_items WHERE id = ANY(:ids)
                RETURNING product_id
            )
            SELECT product_id AS productId, COUNT(1) AS quantity
            FROM deleted
            GROUP BY product_id
            """, nativeQuery = true)
    List<ProQ_ProductId_Quantity> deleteProductItemsByIdIn(@Param("ids") long[] ids);

    @Query(value = """
                DELETE FROM product_items WHERE id = ANY(:ids)
                RETURNING *
            """, nativeQuery = true)
    List<ProductItem> deleteProductItemsByIdInAndReturnAll(@Param("ids") long[] ids);

//    @Query("SELECT p FROM ProductItem p WHERE p.id IN :ids")
//    List<ProductItem> findAllByIdIn(@Param("ids") long[] ids);
//
//    @Modifying(clearAutomatically = true)
//    @Query("DELETE FROM ProductItem p WHERE p.id IN :ids")
//    int deleteByIdIn(@Param("ids") long[] ids);


}