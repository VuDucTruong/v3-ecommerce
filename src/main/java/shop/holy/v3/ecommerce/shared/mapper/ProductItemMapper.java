package shop.holy.v3.ecommerce.shared.mapper;


import jakarta.persistence.criteria.Predicate;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.api.dto.product.item.RequestProductItemCreate;
import shop.holy.v3.ecommerce.api.dto.product.item.RequestProductItemSearch;
import shop.holy.v3.ecommerce.api.dto.product.item.RequestProductItemUpdate;
import shop.holy.v3.ecommerce.api.dto.product.item.ResponseProductItem;
import shop.holy.v3.ecommerce.persistence.entity.ProductItem;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductItemMapper extends IBaseMapper {
    public abstract ResponseProductItem fromEntityToResponse(ProductItem product);

    public abstract ProductItem fromRequestToEntity(RequestProductItemCreate productItem);

    public abstract ProductItem fromUpdateRequestToEntity(RequestProductItemUpdate productItem);

    public Specification<ProductItem> fromRequestSearchToSpec(RequestProductItemSearch searchReq) {
        return (root, query, criteriaBuilder) -> {
            assert query != null;
            if (searchReq == null) return criteriaBuilder.conjunction();

            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.hasLength(searchReq.productKey())) {
                predicate = criteriaBuilder.and(predicate, root.get("productKey").in(searchReq.productKey()));
            }
            if (StringUtils.hasLength(searchReq.productName())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("productName"), "%" + searchReq.productName() + "%"));
            }
            if (!searchReq.used()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("dateUsed")));
            }

            // Filter deleted products
            if (!searchReq.deleted()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("deletedAt")));
            }

            return predicate;
        };
    }

}
