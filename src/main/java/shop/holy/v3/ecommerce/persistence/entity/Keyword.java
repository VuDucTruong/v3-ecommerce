package shop.holy.v3.ecommerce.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "keywords")
@Data
public class Keyword extends EntityBase {

    @Column(name = "value", nullable = false, unique = true)
    private String values;

}
