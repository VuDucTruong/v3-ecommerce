package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Table(name = "coupons")
@Entity
@Getter
@Setter
public class CouponUsage extends EntityBase {

    private String code;
    private String type;
    private LocalDate availableFrom;
    private LocalDate availableTo;
    private BigDecimal value;
    private BigDecimal minAmount;
    private BigDecimal maxAppliedAmount;


}
