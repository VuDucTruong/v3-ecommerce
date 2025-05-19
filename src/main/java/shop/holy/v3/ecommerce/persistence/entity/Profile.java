package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "profiles")
public class Profile extends EntityBase {

    @Column(name = "account_id")
    private Long accountId;

    private String fullName;
    private String imageUrlId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @OneToMany(mappedBy = "profile")
    private Set<Order> orders;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "followers")
    private Set<Product> favoriteProducts;

    @OneToMany(mappedBy = "author")
    private Set<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Profile profile)) return false;
        return Objects.equals(id, profile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
