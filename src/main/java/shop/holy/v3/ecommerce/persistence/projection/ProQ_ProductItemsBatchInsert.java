package shop.holy.v3.ecommerce.persistence.projection;

import java.util.Map;

public record ProQ_ProductItemsBatchInsert(
        long[] productIds,
        String[] productKeys,
        Map<String, String>[] accounts,
        String[] regions
)
{
}
