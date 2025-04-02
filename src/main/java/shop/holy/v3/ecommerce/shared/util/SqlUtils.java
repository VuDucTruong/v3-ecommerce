package shop.holy.v3.ecommerce.shared.util;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

public class SqlUtils {

    public static Predicate lowerCaseLike(CriteriaBuilder criteriaBuilder, Expression<String> expression, String value) {
        return criteriaBuilder.like(criteriaBuilder.lower(expression), "%" + value.toLowerCase() + "%");
    }
}
