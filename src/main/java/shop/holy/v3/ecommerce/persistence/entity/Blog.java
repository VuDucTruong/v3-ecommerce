package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "blogs")
public class Blog extends EntityBase {

    private String title;
    private String subtitle;

    private String content;
    private Date publishedAt;
    private String imageUrlId;
    @Column(name = "profile_id")
    private long profileId;

    @Column(name = "genre_id")
    private Long genreId;

    @ManyToOne()
    @JoinColumn(name = "genre_id", insertable = false, updatable = false)
    private Genre1 genre1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Profile profile;



}