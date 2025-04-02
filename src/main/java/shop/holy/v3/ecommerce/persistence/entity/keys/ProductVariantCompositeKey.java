package shop.holy.v3.ecommerce.persistence.entity.keys;

import lombok.Data;

@Data
public class ProductVariantCompositeKey
{
    private long rootProductId;
    private String groupId;
}
