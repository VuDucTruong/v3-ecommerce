package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment extends EntityBase {

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Column(name = "author_id")
    private long authorId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "content")
    @Length(max = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id",
            referencedColumnName = "id",
            insertable = false, updatable = false)
    private Comment parent;

    @OneToMany(mappedBy = "parent", targetEntity = Comment.class)
    private Set<Comment> replies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private Profile author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Comment comment)) return false;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
