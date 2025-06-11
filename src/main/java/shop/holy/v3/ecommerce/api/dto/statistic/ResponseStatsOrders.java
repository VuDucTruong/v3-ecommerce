package shop.holy.v3.ecommerce.api.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class ResponseStatsOrders {
    private final LocalDate localDate;
    private int pending;
    private int processing;
    private int completed;
    private int failed;

    public ResponseStatsOrders(LocalDate localDate, String status) {
        this.localDate = localDate;
        if (status.equals(OrderStatus.PENDING.name()))
            this.pending = 1;
        if (status.equals(OrderStatus.COMPLETED.name()))
            this.completed = 1;
        else if (status.equals(OrderStatus.FAILED.name()) || status.equals(OrderStatus.FAILED_MAIL.name()))
            this.failed = 1;
        else
            this.processing = 1;
    }
    public void add(ResponseStatsOrders that) {
        pending += that.pending;
        processing += that.processing;
        completed += that.completed;
        failed += that.failed;
    }
}
