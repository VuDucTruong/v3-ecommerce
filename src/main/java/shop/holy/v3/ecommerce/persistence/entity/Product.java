package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "products")
public class Product extends EntityBase {

    @Column(name = "prod_desc_id")
    private Long prodDescId;

    private String slug;
    private String name;
    private String imageUrlId;

    private BigDecimal originalPrice;
    private BigDecimal price;

    private Date availableFrom;
    private Date availableTo;

    @Column(name = "parent_id")
    private long parentId; // Foreign key to ProductVariantGroup

    @OneToMany(mappedBy = "product")
    private List<Category> category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_desc_id", insertable = false, updatable = false)
    private ProductDescription productDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",
            referencedColumnName = "id",
            insertable = false, updatable = false)
    private Product parent;

    @OneToMany(mappedBy = "parent", targetEntity = Product.class)
    private List<Product> variants;

    @ManyToMany
    @JoinTable(
            name = "product_favorites",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private List<Profile> followers;

    @OneToMany(mappedBy = "product")
    private List<Comment> comments;


}
