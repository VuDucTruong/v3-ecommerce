package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
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

    @Column(name = "group_id")
    private Long groupId;

    // JSON
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tags")
    @ColumnDefault(value = "[]")
    private String[] tags;


    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    @BatchSize(size = 20)
    private Set<Category> categories;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "prod_desc_id", insertable = false, updatable = false)
    private ProductDescription productDescription;

    @ManyToMany
    @JoinTable(
            name = "product_favorites",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id")
    )
    private Set<Profile> followers;

    @OneToMany(mappedBy = "product")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "product")
    private Set<ProductItem> productItems;

    @ManyToOne
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private ProductGroup group;


}
