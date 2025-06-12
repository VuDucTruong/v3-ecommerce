package shop.holy.v3.ecommerce.persistence.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductItemUsed;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_Id_ProductId_AcceptedKey;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductItemLight;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductItemUsedRepository extends JpaRepository<ProductItemUsed, Long>, JpaSpecificationExecutor<ProductItemUsed> {

    @Query(value = """
            INSERT INTO product_items_used (product_id, product_key, region, account)
            (SELECT id, key, region, account::jsonb
            FROM unnest(:productIds, :productKeys, :regions,:accounts) as t(id, key, region,account))
            ON CONFLICT (product_id,product_key) DO NOTHING
            RETURNING id, product_key AS accepted_key, product_id
            """, nativeQuery = true)
    List<ProQ_Id_ProductId_AcceptedKey> insertProductItems(
            @Param("productIds") long[] productIds,
            @Param("productKeys") String[] productKeys,
            @Param("accounts") String[] accounts,
            @Param("regions") String[] regions);

    List<ProQ_ProductItemLight> findAllByOrderDetailIdEquals(long orderDetailId);


    Optional<ProQ_ProductItemLight> findFirstById(long id);

    Optional<ProQ_ProductItemLight> findFirstByProductKey(String productKey);
}

