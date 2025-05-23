package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends EntityBase {

    //    @Column(name = "prod_desc_id")
//    private Long prodDescId;
    private String slug;
    private String name;
    private String imageUrlId;
    @ColumnDefault("0")
    private long quantity;

    private BigDecimal originalPrice;
    private BigDecimal price;

    @Column(name = "prod_desc_id")
    private Long proDescId;

    @Column(name = "group_id")
    private Long groupId;

    @ColumnDefault("true")
    private boolean represent;

    // JSON
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tags")
    @ColumnDefault(value = "'[]'")
    private String[] tags;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    @BatchSize(size = 20)
    private Set<Category> categories;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod_desc_id", referencedColumnName = "id", insertable = false, updatable = false)
    private ProductDescription productDescription;

    @ManyToMany(fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "product")
    private Set<ProductItemUsed> productItemUseds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", insertable = false, updatable = false)
    private ProductGroup group;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
