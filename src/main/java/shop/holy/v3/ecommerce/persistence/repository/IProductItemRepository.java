package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.ProductItem;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductId_Quantity;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductId_AcceptedKey;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductItemRepository extends JpaRepository<ProductItem, Long>, JpaSpecificationExecutor<ProductItem> {

//    @Query(value = """
//            select product_id, count(1) as cnt
//                        from product_items where product_id IN :productIds
//                        GROUP BY product_id
//            """, nativeQuery = true)
//    List<ProQ_ProdId_Cnt> find_count_group_by_prodId(long[] productIds);

    Slice<ProductItem> findAllByProductIdEquals(long productId, org.springframework.data.domain.Pageable pageable);

    Optional<ProductItem> findFirstByIdAndDeletedAtIsNull(long id);

    Optional<ProductItem> findFirstByProductKeyAndDeletedAtIsNull(String productKey);

    Optional<ProductItem> findFirstByProductKey(String productKey);

    @Modifying
    @Query("""
            UPDATE ProductItem pi set pi.productKey = :#{#productItem.productKey},
            pi.productId = :#{#productItem.productKey},
            pi.dateUsed = :#{#productItem.dateUsed}
            WHERE pi.id = :#{#productItem.id}
            """)
    int updateProductItemById(@Param("productItem") ProductItem productItem);


    @Query(value = """
            INSERT INTO product_items (product_id, product_key, region)
            (SELECT id, key, region
            FROM unnest(:productIds, :productKeys, :regions) as t(id, key, region))
            ON CONFLICT (product_key) DO NOTHING
            RETURNING product_key AS accepted_key, product_id
            """, nativeQuery = true)
    List<ProQ_ProductId_AcceptedKey> insertProductItems(
            @Param("productIds") long[] productIds,
            @Param("productKeys") String[] productKeys,
            @Param("regions") String[] regions);


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


}