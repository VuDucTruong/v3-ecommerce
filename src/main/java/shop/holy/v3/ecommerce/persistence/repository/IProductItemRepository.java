package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.persistence.entity.ProductItem;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProdId_ProdItem;

import java.util.Optional;

@Repository
public interface IProductItemRepository extends JpaRepository<ProductItem, Long>, JpaSpecificationExecutor<ProductItem> {

    @Query(value = """
            select product_id, count(1) as cnt 
                        from product_items where product_id IN :productIds
                        GROUP BY product_id
            """, nativeQuery = true)
    ProQ_ProdId_ProdItem[] find_count_group_by_prodId(long[] productIds);

    Page<ProductItem> findAllByProductIdEquals(long productId, org.springframework.data.domain.Pageable pageable);

    Optional<ProductItem> findFirstByIdAndDeletedAtIsNull(long id);

    Optional<ProductItem> findFirstByProductKeyAndDeletedAtIsNull(String productKey);

    Optional<ProductItem> findFirstByProductKey(String productKey);

    @Modifying
    @Transactional
//    @Procedure(name = "ProductItem.BATCH_INSERT_RETURN_CONFLICTED_PRODUCT_KEYS")
//    String[] InsertProductWithOnConflictReturning(ProductItem[] productItems);
    @Procedure(name = "ProductItem.insert_product_items_with_conflict_detection")
    String[] insertProductWithOnConflictReturning(long[] productIds, String[] productKeys, String[] regions);


    @Modifying
    int deleteProductItemsByIdIn(long[] ids);

}