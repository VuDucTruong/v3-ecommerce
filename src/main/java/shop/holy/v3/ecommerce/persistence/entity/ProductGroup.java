package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "product_groups")
@Getter
@Setter
public class ProductGroup extends EntityBase {

    private String name;

    @OneToMany(mappedBy = "group")
    @BatchSize(size = 20)
    private Set<Product> variants;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductGroup that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
