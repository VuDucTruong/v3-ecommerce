package shop.holy.v3.ecommerce.persistence.entity.notification;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notification_prod_keys_fail", indexes = {
        @Index(name = "idx_notification_prod_keys_fail_order_id", columnList = "orderId")
})
@Getter
@Setter
public class NotificationProdKeyFail extends NotificationProdKey {
    private String reason;
}