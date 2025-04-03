package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "orders")
public class Order extends EntityBase {

    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "coupon_id")
    private Long couponId;
    private String status;
    private BigDecimal total;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "request_info")
    private Map<String, String> requestInfo;

    @OneToOne
    @JoinColumn(name = "coupon_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Profile profile;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

}
