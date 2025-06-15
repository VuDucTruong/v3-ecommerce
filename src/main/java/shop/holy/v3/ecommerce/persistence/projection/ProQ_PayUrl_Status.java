package shop.holy.v3.ecommerce.persistence.projection;

import java.util.Date;

public interface ProQ_PayUrl_Status {
    String getPaymentUrl();
    String getStatus();
    Date getExpiry();

}
