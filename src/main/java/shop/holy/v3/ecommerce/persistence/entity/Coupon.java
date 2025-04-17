package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Table(name = "coupons")
@Entity
@Data
public class Coupon extends EntityBase {

    private String code;

    private String type;
    private LocalDate availableFrom;
    private LocalDate availableTo;
    private BigDecimal value;
    @ColumnDefault("-1")
    private int minQTY = -1;
    private BigDecimal minAmount;
    private BigDecimal maxAppliedAmount;

    @ColumnDefault("0")
    private int currentUsage;
    private int usageLimit;
    private String description;

    @OneToOne(mappedBy = "coupon")
    private Order order;
}
