package shop.holy.v3.ecommerce.persistence.entity.violation;

public sealed interface ConstraintViolation permits ViolationPrimaryKey, ViolationForeignKey, ViolationUnique {
    String constraintName();
}
