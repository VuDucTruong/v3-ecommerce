package shop.holy.v3.ecommerce.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Component;
import shop.holy.v3.ecommerce.persistence.entity.violation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
@Component
public class ViolationDetectionService {
    @PersistenceContext
    private EntityManager em;
    private Map<String, ConstraintViolation> map;


    public String getErrorMessage(String constraintName) {
        ConstraintViolation violation = map.get(constraintName);
        if (violation == null) {
            return "Unknown constraint violation: " + constraintName;
        }

        return switch (violation) {
            case ViolationPrimaryKey pk -> String.format(
                    "Error when saving entity %s for field %s",
                    pk.tableName(), pk.column()
            );
            case ViolationForeignKey fk -> String.format(
                    "Error, you are asking to save non existed entity %s, requiring a matching %s values",
                    fk.refTable(), fk.refColumn()
            );
            case ViolationUnique uq -> String.format(
                    "Error, you are saving duplicated %s, requiring a unique values for %s",
                    uq.tableName(), String.join(", ", uq.columns())
            );
        };
    }

    @PostConstruct
    protected void initializeViolationMap() {
        List<ConstraintRaw> raws = this.loadAllRawConstraints();
        this.map = raws.stream()
                .map(this::convert)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        ConstraintViolation::constraintName,
                        v -> v
                ));
    }

    private ConstraintViolation convert(ConstraintRaw raw) {
        return switch (raw.constraintType()) {
            case "p" -> new ViolationPrimaryKey(
                    raw.tableName(),
                    raw.constraintName(),
                    raw.columns().split(",")[0].trim()
            );
            case "f" -> new ViolationForeignKey(
                    raw.tableName(),
                    raw.constraintName(),
                    raw.columns().split(",")[0].trim(),
                    raw.referencedTable(),
                    raw.referencedColumns().split(",")[0].trim()
            );
            case "u" -> new ViolationUnique(
                    raw.tableName(),
                    raw.constraintName(),
                    Arrays.stream(raw.columns().split(",")).map(String::trim).toList()
            );
            default -> null;
        };
    }


    private List<ConstraintRaw> loadAllRawConstraints() {
        String sql = """
                SELECT
                    conrelid::regclass::text                                   AS table_name,
                    conname                                                    AS constraint_name,
                    contype::text                                           AS constraint_type,
                    string_agg(att.attname, ',' ORDER BY att.attnum)           AS columns,
                    CASE WHEN contype = 'f' THEN confrelid::regclass::text END AS referenced_table,
                    CASE
                        WHEN contype = 'f' THEN (
                            SELECT string_agg(att2.attname, ',' ORDER BY att2.attnum)
                            FROM unnest(c.confkey) AS colnum
                                     JOIN pg_attribute att2 ON att2.attrelid = c.confrelid AND att2.attnum = colnum
                        )
                        END                                                    AS referenced_columns
                FROM pg_constraint c
                         JOIN pg_namespace ns ON ns.oid = c.connamespace
                         JOIN pg_class cl ON cl.oid = c.conrelid
                         JOIN unnest(c.conkey) AS colnum ON TRUE
                         JOIN pg_attribute att ON att.attrelid = cl.oid AND att.attnum = colnum
                WHERE ns.nspname = 'public'
                GROUP BY conrelid, conname, contype, confrelid, c.confkey;
                """;

        List<Tuple> rows = (List<Tuple>) em.createNativeQuery(sql, Tuple.class).getResultList();

        return rows.stream()
                .map(t -> new ConstraintRaw(
                        t.get("table_name", String.class),
                        t.get("constraint_name", String.class),
                        t.get("constraint_type", String.class),
                        t.get("columns", String.class),
                        t.get("referenced_table", String.class),
                        t.get("referenced_columns", String.class)
                ))
                .toList();
    }


}
