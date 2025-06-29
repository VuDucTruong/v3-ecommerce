package shop.holy.v3.ecommerce.persistence.entity.violation;

import java.util.List;

public record ViolationUnique(
        String tableName,
        String constraintName,
        List<String> columns
) implements ConstraintViolation {}