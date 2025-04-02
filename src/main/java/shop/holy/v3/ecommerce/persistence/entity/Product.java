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


    @Column(name = "tos_id")
    private Long tosId;

    private String slug;
    private String name;
    private String imageUrlId;

    private String description;
    private BigDecimal price;
    private Double discountPercent;

    private Date availableFrom;
    private Date availableTo;

    private String groupId; // Foreign key to ProductVariantGroup

    @OneToMany(mappedBy = "product")
    private List<Category> category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tos_id", insertable = false, updatable = false)
    private TOS tos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "group_id",
                    referencedColumnName = "group_id",
                    insertable = false, updatable = false),
            @JoinColumn(name = "id",
                    referencedColumnName = "root_product_id",
                    insertable = false, updatable = false)
    })
    private ProductVariantGroup productVariantGroup;

}
