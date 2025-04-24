package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "products")
public class Product extends EntityBase {

//    @Column(name = "prod_desc_id")
//    private Long prodDescId;
    private String slug;
    private String name;
    private String imageUrlId;

    private BigDecimal originalPrice;
    private BigDecimal price;

    @Column(name = "group_id")
    private Long groupId;

    private boolean isRepresent;

    // JSON
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tags")
    @ColumnDefault(value = "'[]'")
    private String[] tags;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    @BatchSize(size = 20)
    private Set<Category> categories;

    @OneToOne(mappedBy = "product")
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
