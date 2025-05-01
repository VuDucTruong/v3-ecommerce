package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(name = "product_items")
@Entity
public class ProductItem extends EntityBase {

    @Column(name = "product_id")
    private long productId;

    @Column(name = "product_key", unique = true)
    private String productKey;

    private String region;

    @ColumnDefault("null")
    private Date dateUsed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
}