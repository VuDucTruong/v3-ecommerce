package shop.holy.v3.ecommerce.persistence.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductDescription;

import java.util.Optional;

@Repository
public interface IProductDescriptionRepository extends JpaRepository<ProductDescription, Long> {

    @Query(value = """
            update product_description pd
            set info = :#{#paramPd.info}, description = :#{#paramPd.description}, 
                  policy = :#{#paramPd.policy}, tutorial = :#{#paramPd.tutorial},
                   platform = :#{#paramPd.platform}
            from products p
                    WHERE pd.id = p.prod_desc_id
                    AND p.id = :#{#productId}
            returning pd.*
            """, nativeQuery = true)
    Optional<ProductDescription> updateProductDescriptionByProductId(long productId, @Param("paramPd") ProductDescription productDescription);
}
