package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "comments")
@Data
public class Comment extends EntityBase {

    @Column(name = "parent_comment_id")
    private long parentCommentId;

    @Column(name = "product_id")
    private long authorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id",
            referencedColumnName = "id",
            insertable = false, updatable = false)
    private Comment parent;


    @OneToMany(mappedBy = "parent", targetEntity = Comment.class)
    private Set<Comment> replies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

}
