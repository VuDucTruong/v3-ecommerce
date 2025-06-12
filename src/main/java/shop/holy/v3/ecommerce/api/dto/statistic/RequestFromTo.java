package shop.holy.v3.ecommerce.api.dto.statistic;

import java.time.LocalDate;
import java.util.Objects;

public record RequestFromTo(LocalDate from, LocalDate to) {
    public RequestFromTo(LocalDate from, LocalDate to) {
        this.to = Objects.requireNonNullElseGet(to, LocalDate::now);
        if (from == null || from.isAfter(to))
            this.from = this.to.minusDays(30);
        else
            this.from = from;
    }
}
