package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.ProductGroup;

import java.util.Optional;

@Repository
public interface IProductGroupRepository extends JpaRepository<ProductGroup, Long> {

    Optional<ProductGroup> findFirstByIdAndDeletedAtIsNull(long id);

    @Query("""
            UPDATE ProductGroup pg set pg.name = :name
                   where pg.id = :id
            """)
    int updateProductGroupNameById(String name, long id);

}
