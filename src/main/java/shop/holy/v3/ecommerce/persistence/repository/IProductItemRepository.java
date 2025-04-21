package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.persistence.entity.ProductItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductItemRepository extends JpaRepository<ProductItem, Long>, JpaSpecificationExecutor<ProductItem> {
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