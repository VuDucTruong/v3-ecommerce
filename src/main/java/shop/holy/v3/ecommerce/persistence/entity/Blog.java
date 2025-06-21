package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Getter
@Setter
@Entity
@Table(name = "blogs")
public class Blog extends EntityBase {

    private String title;
    private String subtitle;

    @Length(max = 5000)
    private String content;
    private Date publishedAt;
    private String imageUrlId;
    @Column(name = "profile_id")
    private long profileId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "blogs_genres",
            joinColumns = @JoinColumn(name = "blog_id", insertable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "genre2_id", insertable = false, updatable = false)
    )
    private List<Genre2> genre2s;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Profile profile;

}