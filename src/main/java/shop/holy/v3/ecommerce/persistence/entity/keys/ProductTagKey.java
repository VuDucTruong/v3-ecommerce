package shop.holy.v3.ecommerce.persistence.entity.keys;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class ProductTagKey {
    private long productId;
    private String name;
}
