package shop.holy.v3.ecommerce.shared.constant;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.function.TriFunction;

public class WhereProcessors {
    public static Predicate EQUAL(CriteriaBuilder criteriaBuilder, Expression<?> expression, Object value) {
        return criteriaBuilder.equal(expression, value);
    }
    public static Predicate NOT_EQUAL(CriteriaBuilder criteriaBuilder, Expression<?> expression, Object value) {
        return criteriaBuilder.notEqual(expression, value);
    }


    public static TriFunction<CriteriaBuilder, Expression<?>, String , Predicate> NOT_LIKE = (criteriaBuilder, expression, value)
            -> criteriaBuilder.notLike(criteriaBuilder.lower(expression.as(String.class)), "%" + value.toLowerCase() + "%");
    public static TriFunction<CriteriaBuilder, Expression<?>, String , Predicate> LIKE = (criteriaBuilder, expression, value)
            -> criteriaBuilder.like(criteriaBuilder.lower(expression.as(String.class)), "%" + value.toLowerCase() + "%");

    public static <T extends Comparable<T>> TriFunction<CriteriaBuilder, Expression<T>, T, Predicate> lessThan() {
        return CriteriaBuilder::lessThan;
    }
}
