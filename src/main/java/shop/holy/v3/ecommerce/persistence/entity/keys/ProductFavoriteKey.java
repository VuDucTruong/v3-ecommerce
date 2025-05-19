package shop.holy.v3.ecommerce.persistence.entity.keys;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class ProductFavoriteKey {
    Long profileId;
    Long productId;
}
