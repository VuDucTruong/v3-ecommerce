package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment extends EntityBase {

    @Column(name = "profile_id")
    private Long profileId;
    @Column(name = "order_id")
    private Long orderId;

    @Size(max = 10)
    private String status;
    private String paymentMethod = "VNPAY";
    private String bankCode; // NCB
    private String detailCode;
    private String detailMessage;

    @Size(max = 2048)
    private String paymentUrl;

    private Date expiry;


    @Size(max = 255)
    private String note;
    @Size(max = 50)
    private String cardType;
    @Size(max = 65)
    private String transRef;
    @Size(max = 600)
    private String secureHash;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Profile profile;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Payment that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(profileId);
    }
}
