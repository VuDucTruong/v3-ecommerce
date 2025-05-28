package shop.holy.v3.ecommerce.persistence.repository.product;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductTag;

@Repository
public interface IProductTagRepository extends CrudRepository<ProductTag, Long> {


    void deleteAllByProductId(Long productId);

}
