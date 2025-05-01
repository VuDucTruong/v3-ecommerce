package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
@Entity
@Table(name = "keywords")
@Getter @Setter
public class Keyword extends EntityBase {

    @Column(name = "value", nullable = false, unique = true)
    private String values;

}
