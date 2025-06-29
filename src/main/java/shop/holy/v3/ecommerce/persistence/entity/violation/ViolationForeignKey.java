package shop.holy.v3.ecommerce.persistence.entity.violation;

public record ViolationForeignKey(
        String tableName,
        String constraintName,
        String column,
        String refTable,
        String refColumn
) implements ConstraintViolation {}