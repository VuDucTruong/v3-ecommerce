package shop.holy.v3.ecommerce.persistence.entity.notification;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "notification_prod_keys", indexes = {
        @Index(name = "idx_notification_prod_keys_order_id", columnList = "orderId")
})
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class NotificationProdKey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @ColumnDefault("now()")
    @Column(name = "created_at",insertable = false,updatable = false)
    protected Date createdAt;

    protected String email;
    protected Long orderId;

    protected Date retry1;
    protected Date retry2;

}
