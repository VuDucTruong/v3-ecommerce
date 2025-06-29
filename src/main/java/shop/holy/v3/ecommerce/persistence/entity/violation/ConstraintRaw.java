package shop.holy.v3.ecommerce.persistence.entity.violation;

public record ConstraintRaw(
        String tableName,
        String constraintName,
        String constraintType,
        String columns,             // comma-delimited
        String referencedTable,     // may be null
        String referencedColumns    // comma-delimited, may be null
) {}
