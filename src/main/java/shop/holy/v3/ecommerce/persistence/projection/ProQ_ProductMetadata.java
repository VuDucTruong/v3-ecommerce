package shop.holy.v3.ecommerce.persistence.projection;


import java.util.Date;

public interface ProQ_ProductMetadata {
    long getId();

    long getProductId();

    String getProductKey();

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
