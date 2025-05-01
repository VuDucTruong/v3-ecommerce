package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.Set;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Entity
@Table(name = "product_groups")
@Getter @Setter
public class ProductGroup extends EntityBase {

    private String name;

    @OneToMany(mappedBy = "group")
    @BatchSize(size = 20)
    private Set<Product> variants;

}
