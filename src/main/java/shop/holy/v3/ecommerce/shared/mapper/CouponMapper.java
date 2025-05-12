package shop.holy.v3.ecommerce.shared.mapper;

import jakarta.persistence.criteria.Predicate;
import org.mapstruct.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.coupon.RequestCouponCreate;
import shop.holy.v3.ecommerce.api.dto.coupon.RequestCouponSearch;
import shop.holy.v3.ecommerce.api.dto.coupon.RequestCouponUpdate;
import shop.holy.v3.ecommerce.api.dto.coupon.ResponseCoupon;
import shop.holy.v3.ecommerce.persistence.entity.Coupon;
import shop.holy.v3.ecommerce.shared.util.SqlUtils;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CouponMapper {

    public abstract Coupon fromRequestCreateToEntity(RequestCouponCreate request);

    public abstract Coupon fromRequestUpdateToEntity(RequestCouponUpdate request);

    public abstract ResponseCoupon fromEntityToResponse(Coupon coupon);

    public Specification<Coupon> fromRequestToSpecification(RequestCouponSearch searchReq) {
        return (root, query, criteriaBuilder) -> {
            if (searchReq == null)
                return criteriaBuilder.conjunction();
            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.hasLength(searchReq.search())) {
                predicate = criteriaBuilder.and(predicate,
                        SqlUtils.likeIgnoreCase(criteriaBuilder, root.get("code"), searchReq.search()));
                if (searchReq.type() != null)
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("type"), searchReq.type().name()));
            }

            if (searchReq.availableFrom() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("availableFrom"), searchReq.availableFrom()));
            }
            if (searchReq.availableTo() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("availableTo"), searchReq.availableTo()));
            }

            if (!searchReq.deleted()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("deletedAt")));
            }

            if (searchReq.valueFrom() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("value"), searchReq.valueFrom()));
            }

            if (searchReq.valueFrom() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("value"), searchReq.valueTo()));
            }

            return predicate;
        };
    }


}
