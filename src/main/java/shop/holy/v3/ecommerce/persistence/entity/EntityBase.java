package shop.holy.v3.ecommerce.persistence.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@MappedSuperclass
@Data
public abstract class EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ColumnDefault("now()")
    @Column(name = "created_at",insertable = false,updatable = false)
    private Date createdAt;

    @Column(name = "deleted_at")
    @ColumnDefault("null")
    private Date deletedAt;

}
