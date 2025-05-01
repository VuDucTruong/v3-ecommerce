package shop.holy.v3.ecommerce.shared.mapper;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.mapstruct.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.product.*;
import shop.holy.v3.ecommerce.api.dto.product.description.RequestProductDescription;
import shop.holy.v3.ecommerce.persistence.entity.*;
import shop.holy.v3.ecommerce.shared.constant.MappingFunctions;
import shop.holy.v3.ecommerce.shared.constant.ProductStatus;
import shop.holy.v3.ecommerce.shared.util.SqlUtils;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ProductItemMapper.class, CommentMapper.class, CommonMapper.class})
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductMapper {

    @Mapping(source = "imageUrlId", target = "imageUrl", qualifiedByName = MappingFunctions.GEN_URL)
    @Mapping(source = "group.variants", target = "variants")
    @Mapping(source = "quantity", target = "status", qualifiedByName = "fromCntToStatus")
    @Mapping(source = "productItems", target = "productItems", ignore = true)
    @Mapping(source = "represent", target = "represent")
    public abstract ResponseProduct fromEntityToResponse_Light(Product product);

    @Mapping(source = "product.imageUrlId", target = "imageUrl", qualifiedByName = MappingFunctions.GEN_URL)
    @Mapping(source = "product.group.variants", target = "variants")
    @Mapping(source = "product.quantity", target = "status", qualifiedByName = "fromCntToStatus")
    @Mapping(source = "productItems", target = "productItems")
    public abstract ResponseProduct fromEntity_Items_ToResponse_Detailed(Product product, List<ProductItem> productItems);


//    @Mapping(source = "product.imageUrlId", target = "imageUrl", qualifiedByName = MappingFunctions.GEN_URL)
//    @Mapping(source = "product.group.variants", target = "variants")

    /// /    @Mapping(source = "quantity", target = "quantity")
//    public abstract ResponseProduct fromEntityToResponseWithStatus(Product product);
    @Mapping(source = "productDescription", target = "productDescription", ignore = true)
    public abstract Product fromRequestUpdateToEntity(RequestProductUpdate request);

    @Mapping(source = "productDescription", target = "productDescription", ignore = true)
    public abstract Product fromCreateRequestToEntity(RequestProductCreate request);

    public abstract ProductDescription fromRequestToDescription(RequestProductDescription request);

    @Named("fromCntToStatus")
    public ProductStatus fromCntToStatus(long cnt) {
        return cnt > 0 ? ProductStatus.IN_STOCK : ProductStatus.OUT_OF_STOCK;
    }


    public Specification<Product> fromRequestSearchToSpec(RequestProductSearch searchReq) {
        return ((root, query, criteriaBuilder) -> {
            // Prevent N+1 problems with fetch joins
            assert query != null;
            if (query.getResultType() == Product.class) {
                root.fetch("categories", JoinType.LEFT);
                root.fetch("productDescription", JoinType.LEFT);
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