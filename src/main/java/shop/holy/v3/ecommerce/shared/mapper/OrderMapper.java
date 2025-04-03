package shop.holy.v3.ecommerce.shared.mapper;

import jakarta.persistence.criteria.Predicate;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.order.RequestOrderCreate;
import shop.holy.v3.ecommerce.api.dto.order.RequestOrderSearch;
import shop.holy.v3.ecommerce.api.dto.order.ResponseOrder;
import shop.holy.v3.ecommerce.persistence.entity.Order;
import shop.holy.v3.ecommerce.shared.util.SqlUtils;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class OrderMapper {

    public abstract Order fromCreateRequestToEntity(RequestOrderCreate categoryCreateRequest);

    public abstract ResponseOrder fromEntityToResponse(Order order);

    public Specification<Order> fromRequestSearchToSpec(RequestOrderSearch searchReq) {
        return ((root, query, criteriaBuilder) -> {
            if (searchReq == null) return criteriaBuilder.conjunction();
            Predicate predicate = criteriaBuilder.conjunction();
            if (StringUtils.hasLength(searchReq.search())){
                predicate = criteriaBuilder.and(predicate,
                        SqlUtils.likeIgnoreCase(criteriaBuilder, root.get("id"), searchReq.search()));
            }
            if (searchReq.status() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("status"), searchReq.status()));
            }
            if (searchReq.totalFrom() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("total"), searchReq.totalFrom()));
            }
            if (searchReq.totalTo() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("total"), searchReq.totalTo()));
            }


            return criteriaBuilder.conjunction();
        });
    }


}
