package shop.holy.v3.ecommerce.shared.mapper;

import jakarta.persistence.criteria.Predicate;
import org.mapstruct.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductCreate;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductSearch;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductUpdate;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.persistence.entity.Product;
import shop.holy.v3.ecommerce.shared.util.SqlUtils;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductMapper extends IBaseMapper {

    @Mapping(source = "imageUrlId", target = "imageUrl", qualifiedByName = "genUrl")

    public abstract ResponseProduct fromEntityToResponse(Product product);

    public abstract Product fromRequestUpdateToEntity(RequestProductUpdate request);

    public abstract Product fromCreateRequestToEntity(RequestProductCreate request);


    public Specification<Product> fromRequestSearchToSpec(RequestProductSearch searchReq) {
        return ((root, query, criteriaBuilder) -> {
            if (searchReq == null) return criteriaBuilder.conjunction();
            Predicate predicate = criteriaBuilder.conjunction();
            if (StringUtils.hasLength(searchReq.search())) {
                predicate = criteriaBuilder.and(predicate, root.get("id").in(searchReq.categoryIds()));
            }

            if (StringUtils.hasLength(searchReq.search())) {
                predicate = criteriaBuilder.and(predicate,
                        SqlUtils.likeIgnoreCase(criteriaBuilder, root.get("name"), searchReq.search()));
                predicate = criteriaBuilder.and(predicate,
                        SqlUtils.likeIgnoreCase(criteriaBuilder, root.get("slug"), searchReq.search()));
            }
            if (searchReq.priceFrom() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("price"), searchReq.priceFrom()));
            }
            if (searchReq.priceTo() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("price"), searchReq.priceTo()));
            }
            if (searchReq.availableFrom() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("availableFrom"), searchReq.availableFrom()));
            }
            if (searchReq.availableTo() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("availableTo"), searchReq.availableTo()));
            }
            if (!searchReq.deleted()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("deletedAt")));
            }


            return predicate;
        });
    }

//    public abstract ProductChangesResponse fromEntityToChangesResponse(Product product);
//    public abstract ProductImageResponse fromImageToImageResponse(ProductImage image);


}
