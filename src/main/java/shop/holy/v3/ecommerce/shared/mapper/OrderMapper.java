package shop.holy.v3.ecommerce.shared.mapper;

import jakarta.persistence.criteria.Predicate;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.order.RequestOrderCreate;
import shop.holy.v3.ecommerce.api.dto.order.RequestOrderSearch;
import shop.holy.v3.ecommerce.api.dto.order.ResponseOrder;
import shop.holy.v3.ecommerce.persistence.entity.Order;
import shop.holy.v3.ecommerce.persistence.entity.OrderDetail;
import shop.holy.v3.ecommerce.persistence.entity.Product;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;
import shop.holy.v3.ecommerce.shared.constant.PaymentStatus;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;
import shop.holy.v3.ecommerce.shared.util.SqlUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CommonMapper.class})
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class OrderMapper {

    @Mapping(source = "orderDetails", target = "orderDetails", ignore = true)
    @Mapping(target = "status", constant = "PROCESSING")
    public abstract Order fromCreateRequestToEntity(RequestOrderCreate categoryCreateRequest);

    @Mapping(source = "details", target = "details")
    public abstract ResponseOrder fromEntityToResponse_InDetail(Order order, Collection<OrderDetail> details);

    @Mapping(source = "status", target = "status")
    @Mapping(source = "orderDetails", target = "details", ignore = true)
    public abstract ResponseOrder fromEntityToResponse_Light(Order order);

    public Map<Long, OrderDetail> fromRequestToOrderDetails(List<RequestOrderCreate.RequestOrderDetail> details) {
        return details.stream().collect(Collectors.toMap(RequestOrderCreate.RequestOrderDetail::productId, this::fromRequestToOrderDetail));
    }

    public abstract OrderDetail fromRequestToOrderDetail(RequestOrderCreate.RequestOrderDetail detail);

    @Mapping(source = "product.imageUrlId", target = "imageUrl", qualifiedByName = "genUrl")
    protected abstract ResponseOrder.ResponseOrderItem productToResponseOrderItem(Product product);

//    protected abstract ResponseOrder.ResponseOrderDetail orderDetailToResponseOrderDetail(OrderDetail orderDetail);


    public Specification<Order> fromRequestSearchToSpec(RequestOrderSearch searchReq) {
        return ((root, query, criteriaBuilder) -> {
            if (searchReq == null) return criteriaBuilder.conjunction();
            Predicate predicate = criteriaBuilder.conjunction();
            if (StringUtils.hasLength(searchReq.search())) {
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
            if (!searchReq.deleted()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("deletedAt")));
            }
            AuthAccount authAccount = SecurityUtil.getAuthNonNull();
            if (Objects.equals(authAccount.getRole(), RoleEnum.CUSTOMER)) {
                predicate = criteriaBuilder.equal(root.get("accountId"), authAccount.getId());
            }

            return predicate;
        });
    }


}
