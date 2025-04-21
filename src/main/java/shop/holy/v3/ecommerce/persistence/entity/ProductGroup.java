package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Entity
@Table(name = "product_groups")
@Data
public class ProductGroup extends EntityBase {

    private String name;

    @OneToMany(mappedBy = "group")
    private Set<Product> products;


}
