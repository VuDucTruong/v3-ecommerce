//package shop.holy.v3.ecommerce.shared.util;
//
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.Expression;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
//import org.apache.commons.lang3.function.TriFunction;
//
//public class DynamicSpecificationUtils {
//
//
//    public static <X extends Comparable<X>, T> Predicate createPredicate(
//            Expression<X> expression,
//
//            CriteriaBuilder criteriaBuilder) {
//
//        TriFunction<CriteriaBuilder, Expression<?>, Object, Predicate> function = spec.getComparison().getFunc();
//
//        if (function == null) {
//            throw new UnsupportedOperationException("Unsupported comparison operation: " + spec.getComparison());
//        }
//        Object value = spec.getValue();
//        if (spec.getType() != null) {
//            value = parsers.get(spec.getType()).apply(spec.getValue());
//        }
//        Predicate predicate = function.apply(criteriaBuilder, expression, value);
//        return spec.isOpposite() ? criteriaBuilder.not(predicate) : predicate;
//    }
//
//
//}
