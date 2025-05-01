package shop.holy.v3.ecommerce.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
public abstract class EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @lombok.EqualsAndHashCode.Include
    private long id;

    @ColumnDefault("now()")
    @Column(name = "created_at",insertable = false,updatable = false)
    private Date createdAt;

    @Column(name = "deleted_at")
    @ColumnDefault("null")
    private Date deletedAt;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EntityBase that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
