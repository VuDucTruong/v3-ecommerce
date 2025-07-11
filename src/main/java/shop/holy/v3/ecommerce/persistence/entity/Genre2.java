package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter @Setter
@Entity
@Table(name = "genre2", uniqueConstraints = @UniqueConstraint(columnNames = {"genre1_id", "name"}))
public class Genre2 extends EntityBase {
    @Column(name = "name")
    private String name;

    @Column(name = "genre1_id")
    private Long genre1Id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Genre1.class)
    @JoinColumn(name = "genre1_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Genre1 genre1;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "genre2s")
    private java.util.List<Blog> blogs;

}
