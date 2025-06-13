package shop.holy.v3.ecommerce.persistence.projection;

import shop.holy.v3.ecommerce.persistence.entity.product.Product;

import java.math.BigDecimal;

public interface ProQ_TotalSold_Product {
    Long getId();
    String getSlug();
    String getName();
    String getImageUrlId();
    long getQuantity();
    BigDecimal getOriginalPrice();
    BigDecimal getPrice();
    Long getProDescId();
    Long getGroupId();
    boolean isRepresent();
    Long getTotalSold();

}
