package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Entity
@Data
@Table(name = "categories")
public class Category extends EntityBase {

    @Column(name = "product_id")
    private Long productId;

    private String name;
    private String description;
    private String imageUrlId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "products_categories",
            uniqueConstraints = @UniqueConstraint(columnNames = {"product_id", "category_id"}),
            joinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private List<Product> products;
}
