package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Table(name = "product_items")
@Entity
@NamedStoredProcedureQuery(
        name = "ProductItem.insert_product_items_with_conflict_detection",
        procedureName = "insert_product_items_with_conflict_detection",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "product_ids", type = Long[].class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "product_keys", type = String[].class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "regions", type = String[].class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "rejected_product_key", type = String[].class)
        }
)
public class ProductItem extends EntityBase {

    @Column(name = "product_id")
    private long productId;

    @Column(name = "product_key", unique = true)
    private String productKey;

    private String region;

    @ColumnDefault("null")
    private Date dateUsed;

    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
}