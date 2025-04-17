package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Data
@Entity
@Table(name = "blogs")
public class Blog extends EntityBase {

    private String title;
    private String subtitle;

    private String content;
    private Date publishedAt;
    private String imageUrlId;

    @OneToMany(mappedBy = "blog")
    private List<Genre> genres;
}
