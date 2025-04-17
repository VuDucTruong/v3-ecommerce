package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Entity
@Table(name = "product_description")
@Data
public class ProductDescription extends EntityBase {

    @Column(name = "description")
    private String description;


    @Column(name = "info")
    private String info;


    @Column(name = "platform")
    private String platform;

    @Column(name = "policy")
    private String policy;


    @Column(name = "tutorial")
    private String tutorial;

    @OneToOne(mappedBy = "productDescription")
    private Product product;
}
