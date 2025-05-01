package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category extends EntityBase {

    private String name;
    private String description;
    private String imageUrlId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "products_categories",
            uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "category_id"}),
            joinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Product> products;
}
