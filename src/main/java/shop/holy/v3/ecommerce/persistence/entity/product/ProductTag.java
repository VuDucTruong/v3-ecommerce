package shop.holy.v3.ecommerce.persistence.entity.product;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import shop.holy.v3.ecommerce.persistence.entity.keys.ProductTagKey;

import java.util.Objects;

@IdClass(ProductTagKey.class)
@Getter
@Setter
@Entity
@Table(name = "product_tags")
public class ProductTag {
    @Id
    @Column(name = "product_id")
    private long productId;
    @Id
    @Length(max = 30)
    @Column(name = "name")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductTag that)) return false;
        return productId == that.productId && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name);
    }
}
