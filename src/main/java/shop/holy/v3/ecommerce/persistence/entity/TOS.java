package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "tos")
@Data
public class TOS extends EntityBase {

    private String content;
    private String title;
    private String subtitle;

    @OneToMany(mappedBy = "tos")
    private List<Product> product;
}
