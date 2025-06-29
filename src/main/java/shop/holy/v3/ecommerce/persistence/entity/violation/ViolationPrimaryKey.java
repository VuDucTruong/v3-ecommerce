package shop.holy.v3.ecommerce.persistence.entity.violation;

public record ViolationPrimaryKey(
        String tableName,
        String constraintName,
        String column
) implements ConstraintViolation {}