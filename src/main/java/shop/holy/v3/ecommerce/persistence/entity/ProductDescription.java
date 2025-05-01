package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.type.SqlTypes;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "product_description")
@Getter @Setter
public class ProductDescription extends EntityBase {

    @Column(name = "description")
    private String description;

    @Column(name = "info")
    private String info;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "platform")
    private String platform;

    @Column(name = "policy")
    private String policy;

    @Column(name = "tutorial")
    private String tutorial;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;
}
