package shop.holy.v3.ecommerce.api.dto.statistic;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequestStatsOrders {
    private LocalDate from;
    private LocalDate to;
}
