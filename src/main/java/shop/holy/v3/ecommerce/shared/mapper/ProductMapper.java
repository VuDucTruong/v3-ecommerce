package shop.holy.v3.ecommerce.shared.mapper;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.mapstruct.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductCreate;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductSearch;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductUpdate;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.persistence.entity.Category;
import shop.holy.v3.ecommerce.persistence.entity.Product;
import shop.holy.v3.ecommerce.shared.util.SqlUtils;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductMapper extends IBaseMapper {

    @Mapping(source = "imageUrlId", target = "imageUrl", qualifiedByName = "genUrl")
    public abstract ResponseProduct fromEntityToResponse(Product product);

    public abstract Product fromRequestUpdateToEntity(RequestProductUpdate request);

    public abstract Product fromCreateRequestToEntity(RequestProductCreate request);


    public Specification<Product> fromRequestSearchToSpec(RequestProductSearch searchReq) {
        return ((root, query, criteriaBuilder) -> {
            // Prevent N+1 problems with fetch joins
            assert query != null;
            if (query.getResultType() == Product.class) {
                root.fetch("categories", JoinType.LEFT);
                root.fetch("productDescription", JoinType.LEFT);
                root.fetch("variants", JoinType.LEFT);
            }

            if (searchReq == null) return criteriaBuilder.conjunction();

            Predicate predicate = criteriaBuilder.conjunction();

            // Filter by IDs
            if (!CollectionUtils.isEmpty(searchReq.ids())) {
                predicate = criteriaBuilder.and(predicate, root.get("id").in(searchReq.ids()));
            }

            // Filter by category IDs
            if (!CollectionUtils.isEmpty(searchReq.categoryIds())) {
                Join<Product, Category> categoryJoin = root.join("categories", JoinType.INNER);
                predicate = criteriaBuilder.and(predicate, categoryJoin.get("id").in(searchReq.categoryIds()));
            }

            // Search by name or slug
            if (StringUtils.hasLength(searchReq.search())) {
                Predicate namePredicate = SqlUtils.likeIgnoreCase(criteriaBuilder, root.get("name"), searchReq.search());
                Predicate slugPredicate = SqlUtils.likeIgnoreCase(criteriaBuilder, root.get("slug"), searchReq.search());
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.or(namePredicate, slugPredicate));
            }

            // Price range filter
            if (searchReq.priceFrom() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("price"), searchReq.priceFrom()));
            }
            if (searchReq.priceTo() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("price"), searchReq.priceTo()));
            }



            // Filter deleted products
            if (!searchReq.deleted()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("deletedAt")));
            }

            return predicate;
        });
    }

//    public abstract ProductChangesResponse fromEntityToChangesResponse(Product product);
//    public abstract ProductImageResponse fromImageToImageResponse(ProductImage image);


}