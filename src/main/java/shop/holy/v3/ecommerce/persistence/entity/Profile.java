package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Entity
@Data
@Table(name = "profiles")
public class Profile extends EntityBase {

    @Column(name = "account_id")
    private Long accountId;

    private String fullName;
    private String imageUrlId;
    private String phone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account account;

    @OneToMany(mappedBy = "profile")
    private Set<Order> orders;

    @ManyToMany(mappedBy = "followers")
    private Set<Product> favoriteProducts;

    @OneToMany(mappedBy = "author")
    private Set<Comment> comments;

}
