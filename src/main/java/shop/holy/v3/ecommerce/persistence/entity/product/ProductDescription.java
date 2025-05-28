package shop.holy.v3.ecommerce.persistence.entity.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import shop.holy.v3.ecommerce.persistence.entity.EntityBase;

import java.util.Objects;

@Entity
@Table(name = "product_description")
@Getter
@Setter
public class ProductDescription extends EntityBase {

    @Column(name = "description")
    @Length(max = 5000)
    private String description;

    @Column(name = "info")
    @Length(max = 5000)
    private String info;

    @Column(name = "platform")
    @Length(max = 5000)
    private String platform;

    @Column(name = "policy")
    @Length(max = 5000)
    private String policy;

    @Column(name = "tutorial")
    @Length(max = 5000)
    private String tutorial;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
//    private Product product;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductDescription that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
