package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "categories")
public class Category extends EntityBase {

    @Column(name = "product_id")
    private Long productId;

    private String name;
    private String description;
    private String imageUrlId;

    @ManyToOne
    @JoinColumn(name = "product_id",insertable = false,updatable = false)
    private Product product;
}
