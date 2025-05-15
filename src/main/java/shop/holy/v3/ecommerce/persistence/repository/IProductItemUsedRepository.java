package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.ProductItemUsed;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductId_AcceptedKey;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductMetadata;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IProductItemUsedRepository extends JpaRepository<ProductItemUsed, Long>, JpaSpecificationExecutor<ProductItemUsed> {

    @Query(value = """
            INSERT INTO product_items_used (product_id, product_key, region)
            (SELECT id, key, region
            FROM unnest(:productIds, :productKeys, :regions) as t(id, key, region))
            ON CONFLICT (product_key) DO NOTHING
            RETURNING product_id ,product_key AS accepted_key
            """, nativeQuery = true)
    List<ProQ_ProductId_AcceptedKey> insertProductItems(
            @Param("productIds") long[] productIds,
            @Param("productKeys") String[] productKeys,
            @Param("regions") String[] regions);

    List<ProQ_ProductMetadata> findAllByOrderDetailIdEquals(long orderDetailId);


    Optional<ProQ_ProductMetadata> findFirstById(long id);

    Optional<ProQ_ProductMetadata> findFirstByProductKey(String productKey);
}

