package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.*;


@Getter
@Setter
@Entity
@Table(name = "genre1")
public class Genre1 extends EntityBase {

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "genre1", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Genre2> genre2s = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Genre1 that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
