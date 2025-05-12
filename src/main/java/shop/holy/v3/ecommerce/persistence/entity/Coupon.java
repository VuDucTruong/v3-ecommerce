package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Table(name = "coupons")
@Entity
@Getter
@Setter
public class Coupon extends EntityBase {

    private String code;

    private String type;
    private LocalDate availableFrom;
    private LocalDate availableTo;
    private BigDecimal value;

    private BigDecimal minAmount;
    private BigDecimal maxAppliedAmount;

    @ColumnDefault("0")
    private int currentUsage;
    private int usageLimit;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coupon coupon)) return false;
        return Objects.equals(id, coupon.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
