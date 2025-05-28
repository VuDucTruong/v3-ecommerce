package shop.holy.v3.ecommerce.persistence.entity.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import shop.holy.v3.ecommerce.persistence.entity.OrderDetail;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Table(name = "product_items_used")
@Entity
public class ProductItemUsed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ColumnDefault("now()")
    @Column(name = "created_at",insertable = false,updatable = false)
    private Date createdAt;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "product_key")
    private String productKey;

    private String region;

    @Column(name = "order_detail_id", nullable = true)
    private Long orderDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id", insertable = false, updatable = false)
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductItemUsed that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
