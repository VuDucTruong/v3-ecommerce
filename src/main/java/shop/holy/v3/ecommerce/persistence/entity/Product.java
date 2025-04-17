package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
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
    private Long parentId; // Foreign key to ProductVariantGroup

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    @BatchSize(size = 20)
    private Set<Category> categories;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "prod_desc_id", insertable = false, updatable = false)
    private ProductDescription productDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id",
            referencedColumnName = "id",
            insertable = false, updatable = false)
    private Product parent;

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "parent", targetEntity = Product.class)
    private Set<Product> variants;

    @ManyToMany
    @JoinTable(
            name = "product_favorites",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private Set<Profile> followers;

    @OneToMany(mappedBy = "product")
    private Set<Comment> comments;


}
