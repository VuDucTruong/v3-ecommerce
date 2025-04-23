package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Entity
@Table(name = "product_groups")
@Data
public class ProductGroup extends EntityBase {

    private String name;

    @Column(name = "represent_id", nullable = true)
    private Long representId;

    @OneToMany(mappedBy = "group")
    private Set<Product> variants;

    @OneToOne()
    @JoinColumn(name = "represent_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product represent;



}
