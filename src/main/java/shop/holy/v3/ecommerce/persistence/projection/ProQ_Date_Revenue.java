package shop.holy.v3.ecommerce.persistence.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ProQ_Date_Revenue {
    LocalDate getDate();
    BigDecimal getRevenue();

}
