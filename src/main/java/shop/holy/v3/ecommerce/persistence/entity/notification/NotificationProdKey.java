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
@Table(name = "notification_prod_keys")
@Getter
@Setter
public class NotificationProdKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ColumnDefault("now()")
    @Column(name = "created_at",insertable = false,updatable = false)
    protected Date createdAt;

    private String email;
    private Long orderId;


}
