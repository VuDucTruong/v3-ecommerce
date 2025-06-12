package shop.holy.v3.ecommerce.persistence.projection;


import java.util.Date;
import java.util.Map;

public interface ProQ_ProductItemLight {
    long getId();

    long getProductId();

    String getProductKey();

    Map<String, String> getAccount();

    String getRegion();

    ProductInfo getProduct();

    Date getCreatedAt();


    interface ProductInfo {
        String getName();

        String getSlug();

        String getImageUrlId();

        String getPrice();

        String getOriginalPrice();

        boolean isRepresent();

    }


}
