package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "product_description")
@Data
public class ProductDescription extends EntityBase {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "description")
    private String description;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "info")
    private String info;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "platform")
    private String platform;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "policy")
    private String policy;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tutorial")
    private String tutorial;

    @OneToOne(mappedBy = "productDescription")
    private Product product;
}
