package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "genre")
public class Genre extends EntityBase {

    @Column(name = "name")
    private String name;

    @Column(name = "genre2_id")
    private Long genre2Id;

    @Column(name = "blog_id")
    private String blogId;

    @ManyToOne
    @JoinColumn(name = "blog_id", insertable = false, updatable = false)
    private Blog blog;

    @OneToMany(mappedBy = "genre")
    private List<Genre> genre2List;

    @ManyToOne
    @JoinColumn(name = "genre2_id", insertable = false, updatable = false)
    private Genre genre;


}
