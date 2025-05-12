package shop.holy.v3.ecommerce.persistence.projection;

public interface ProQ_OrderDetails {
    long getId();

    long getOrderId();

    long getProductId();

    int getQuantity();
}
