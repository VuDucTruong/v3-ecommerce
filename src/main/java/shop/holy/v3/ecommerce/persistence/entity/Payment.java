package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends EntityBase {

    @Column(name = "profile_id")
    private Long profileId;
    @Column(name = "order_id")
    private Long orderId;

    private int status = 2;
    private String paymentMethod = "VNPAY";
    private String bankCode;

    @Size(max = 255)
    private String orderInfo;
    @Size(max = 50)
    private String cardMethod;
    @Size(max = 65)
    private String transRef;
    @Size(max = 256)
    private String secureHash;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Profile profile;

}
