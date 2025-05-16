package shop.holy.v3.ecommerce.api.dto.statistic;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ResponseStatistics(
        ResponseStatTotal totals,
        List<ResposneStatRevenues> revenues
) {


    public record ResposneStatRevenues(
            LocalDate date,
            BigDecimal value
    ){}

}
