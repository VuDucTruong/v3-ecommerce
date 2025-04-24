package shop.holy.v3.ecommerce.shared.mapper;


import jakarta.persistence.criteria.Predicate;
import org.mapstruct.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.product.item.RequestProductItemCreate;
import shop.holy.v3.ecommerce.api.dto.product.item.RequestProductItemSearch;
import shop.holy.v3.ecommerce.api.dto.product.item.RequestProductItemUpdate;
import shop.holy.v3.ecommerce.api.dto.product.item.ResponseProductItem;
import shop.holy.v3.ecommerce.persistence.entity.ProductItem;

import java.util.Date;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductItemMapper extends IBaseMapper {

    @Mapping(source = "dateUsed", target = "active", qualifiedByName = "fromDateUsedToActive")
    public abstract ResponseProductItem fromEntityToResponse(ProductItem product);

    public abstract ProductItem fromRequestToEntity(RequestProductItemCreate productItem);


    public abstract ProductItem fromUpdateRequestToEntity(RequestProductItemUpdate productItem);

    ///  if used -> date_used !=null -> not active
    /// ->date_used == nul -> active
    @Named("fromDateUsedToActive")
    public boolean fromDateUsedToActive(Date dateUsed) {
        return dateUsed == null;
    }

    public Specification<ProductItem> fromRequestSearchToSpec(RequestProductItemSearch searchReq) {
        return (root, query, criteriaBuilder) -> {
            assert query != null;
            if (searchReq == null) return criteriaBuilder.conjunction();

            Predicate predicate = criteriaBuilder.conjunction();

            if(searchReq.productId()!=null){
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("productId"), searchReq.productId()));
            }


            if (StringUtils.hasLength(searchReq.productKey())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("productKey"),
                        "%" + searchReq.productKey() + "%"));
            }
            if (StringUtils.hasLength(searchReq.productName())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("product").get("name"),
                        "%" + searchReq.productName() + "%"));
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
