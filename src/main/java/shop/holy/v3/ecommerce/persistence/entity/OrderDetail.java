package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
@Getter @Setter
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id",nullable = false)
    private long orderId;

    private long productId;
    private BigDecimal originalPrice;
    private BigDecimal price;

    @Column(name = "quantity",nullable = false)
    @ColumnDefault(value = "1")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",insertable = false,updatable = false)
    private Order order;

}
