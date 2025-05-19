package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import shop.holy.v3.ecommerce.persistence.entity.keys.ProductFavoriteKey;

import java.util.Date;


@Getter
@Setter
@Entity
@IdClass(ProductFavoriteKey.class)
@Table(name = "product_favorites")
public class ProductFavorite {
    @Id
    @Column(name = "product_id")
    private Long productId;

    @Id
    @Column(name = "profile_id")
    private Long profileId;

    @ColumnDefault("now()")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("profileId")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private Product product;

}