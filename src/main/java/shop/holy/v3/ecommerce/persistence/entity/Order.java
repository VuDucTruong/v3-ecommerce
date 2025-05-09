package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends EntityBase {

    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "coupon_id")
    private Long couponId;
    private BigDecimal originalAmount;
    private BigDecimal amount;
    private String status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "request_info")
    private Map<String, String> requestInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Profile profile;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    @OneToMany(mappedBy = "order")
    private Set<OrderDetail> orderDetails;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
