package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Table(name = "product_items",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"product_id", "product_key"})
        })
@Entity
public class ProductItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @ColumnDefault("now()")
    @Column(name = "created_at", insertable = false, updatable = false)
    protected Date createdAt;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "product_key", nullable = false)
    private String productKey;

    private String region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductItem that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}