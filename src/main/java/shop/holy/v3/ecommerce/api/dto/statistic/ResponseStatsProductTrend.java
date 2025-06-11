package shop.holy.v3.ecommerce.api.dto.statistic;

public record ResponseStatsProductTrend(
        long saleCount,
        ProductTrend product
) {
    public record ProductTrend(long id, String name) {
    }
}
