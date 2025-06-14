package shop.holy.v3.ecommerce.api.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTopProductSold
{
    long id;
    String slug;
    String name;
    String imageUrl;
    long quantity;

    boolean represent;
    BigDecimal price;
    BigDecimal originalPrice;
    Long totalSold;
}
