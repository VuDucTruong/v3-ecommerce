package shop.holy.v3.ecommerce.persistence.entity.notification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "notification_prod_keys_success", indexes = {
        @Index(name = "idx_notification_prod_keys_success_order_id", columnList = "orderId")
})
@Getter
@Setter
public class NotificationProdKeySuccess extends NotificationProdKey {


    @Length(max = 3000)
    String mailProdKeyJson;


}
