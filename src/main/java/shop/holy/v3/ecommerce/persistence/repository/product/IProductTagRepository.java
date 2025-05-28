package shop.holy.v3.ecommerce.persistence.repository.product;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductTag;

import java.util.List;

@Repository
public interface IProductTagRepository extends CrudRepository<ProductTag, Long> {

    @Query("SELECT distinct pt.name FROM ProductTag pt ")
    List<String> findALlDistinctName();


    void deleteAllByProductId(Long productId);

}
