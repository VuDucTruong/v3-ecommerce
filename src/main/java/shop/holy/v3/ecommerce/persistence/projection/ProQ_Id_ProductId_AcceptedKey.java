package shop.holy.v3.ecommerce.persistence.projection;

import java.util.Map;

public interface ProQ_Id_ProductId_AcceptedKey {
    Long getId();
    Long getProductId();
    String getAcceptedKey();
    Map<String, String> getAccount();
}
