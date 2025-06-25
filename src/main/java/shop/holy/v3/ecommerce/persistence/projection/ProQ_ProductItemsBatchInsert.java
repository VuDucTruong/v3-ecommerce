package shop.holy.v3.ecommerce.persistence.projection;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public record ProQ_ProductItemsBatchInsert(
        long[] productIds,
        String[] productKeys,
        Map<String, String>[] accounts,
        String[] regions
)
{
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProQ_ProductItemsBatchInsert(
                long[] ids, String[] keys, Map<String, String>[] accounts1, String[] regions1
        ))) return false;
        return Objects.deepEquals(regions, regions1) && Objects.deepEquals(productIds, ids) && Objects.deepEquals(productKeys, keys) && Objects.deepEquals(accounts, accounts1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(productIds), Arrays.hashCode(productKeys), Arrays.hashCode(accounts), Arrays.hashCode(regions));
    }
}
