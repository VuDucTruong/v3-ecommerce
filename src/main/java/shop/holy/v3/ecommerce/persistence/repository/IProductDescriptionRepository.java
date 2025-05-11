package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.ProductDescription;

@Repository
public interface IProductDescriptionRepository extends JpaRepository<ProductDescription, Long> {
    @Query("""
            update ProductDescription pd 
            set pd.info = :#{#paramPd.info}, pd.description = :#{#paramPd.description}, 
                  pd.policy = :#{#paramPd.policy}, pd.tutorial = :#{#paramPd.tutorial},
                   pd.platform = :#{#paramPd.platform}
                    WHERE pd.id = :#{#paramPd.id}
            """)
    int updateProductDescriptionById(@Param("paramPd") ProductDescription productDescription);
}
