package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.HashSet;
import java.util.Set;


@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "genre1")
public class Genre1 extends EntityBase {

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "genre1", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Genre2> genre2s = new HashSet<>();


}
