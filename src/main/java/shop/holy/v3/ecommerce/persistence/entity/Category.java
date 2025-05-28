package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import shop.holy.v3.ecommerce.persistence.entity.product.Product;

import java.util.Objects;
import java.util.Set;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Category category)) return false;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
