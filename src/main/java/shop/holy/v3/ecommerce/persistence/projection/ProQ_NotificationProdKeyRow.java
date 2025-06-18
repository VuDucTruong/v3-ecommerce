package shop.holy.v3.ecommerce.persistence.projection;

import java.util.Date;

public record ProQ_NotificationProdKeyRow(Long id,
                                          Date createdAt,
                                          String email,
                                          Long orderId,
                                          Date retry1,
                                          Date retry2) {

}
