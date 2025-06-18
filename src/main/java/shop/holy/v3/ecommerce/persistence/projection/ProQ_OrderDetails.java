package shop.holy.v3.ecommerce.persistence.projection;

public class ProQ_OrderDetails {
    private final Long id;
    private final Long orderId;
    private final Long productId;
    private final String productName;
    private final String imageUrlId;
    private final Integer quantity;

    public ProQ_OrderDetails(Long id, Long orderId, Long productId, String productName, String imageUrlId, Integer quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.imageUrlId = imageUrlId;
        this.quantity = quantity;
    }

    // ✅ Getters are required for Spring to map it properly
    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getImageUrlId() { return imageUrlId; }
    public Integer getQuantity() { return quantity; }
}