package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import shop.holy.v3.ecommerce.persistence.entity.keys.ProductVariantCompositeKey;

import java.util.List;

@Entity
@Table(name = "product_variant_groups")
@Data
@IdClass(ProductVariantCompositeKey.class)
public class ProductVariantGroup {

    @Id
    @Column(name = "root_product_id")
    private long rootProductId;

    @Id
    @Column(name = "group_id")
    private String groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_product_id",
            referencedColumnName = "id",
            insertable = false, updatable = false)
    private Product parent;

    @OneToMany(mappedBy = "productVariantGroup", targetEntity = Product.class)
    private List<Product> variants;
}
