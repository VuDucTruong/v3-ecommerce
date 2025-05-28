package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import shop.holy.v3.ecommerce.persistence.entity.product.Product;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductItemUsed;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "order_details")
@Getter
@Setter
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private long orderId;

    private long productId;
    private BigDecimal originalPrice;
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    @ColumnDefault(value = "1")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @Transient
    private Product product;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderDetail")
    private Set<ProductItemUsed> usedProductItems;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OrderDetail that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
