package shop.holy.v3.ecommerce.shared.mapper.product;

import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.mapstruct.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.product.*;
import shop.holy.v3.ecommerce.api.dto.product.description.RequestProductDescription;
import shop.holy.v3.ecommerce.persistence.entity.Category;
import shop.holy.v3.ecommerce.persistence.entity.product.Product;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductDescription;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductItem;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductTag;
import shop.holy.v3.ecommerce.shared.constant.MapFuncs;
import shop.holy.v3.ecommerce.shared.constant.ProductStatus;
import shop.holy.v3.ecommerce.shared.mapper.CommentMapper;
import shop.holy.v3.ecommerce.shared.mapper.CommonMapper;
import shop.holy.v3.ecommerce.shared.util.SqlUtils;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ProductItemMapper.class, CommonMapper.class, CommentMapper.class, ProductTagMapper.class,})
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductMapper {

    @Mappings({
            @Mapping(source = "product.imageUrlId", target = "imageUrl", qualifiedByName = MapFuncs.GEN_URL),
            @Mapping(source = "product.quantity", target = "status", qualifiedByName = "fromCntToStatus"),
            @Mapping(source = "product.productItems", target = "productItems", ignore = true),
            @Mapping(source = "product.represent", target = "represent"),
            @Mapping(source = "favorite", target = "favorite"),
            @Mapping(target = "tags", source = "tags"),
    })
    public abstract ResponseProduct fromEntityToResponse_Light(Product product, String[] tags, Boolean favorite);

    @Mappings({
            @Mapping(source = "product.imageUrlId", target = "imageUrl", qualifiedByName = MapFuncs.GEN_URL),
            @Mapping(source = "product.group.variants", target = "variants"),
            @Mapping(source = "product.quantity", target = "status", qualifiedByName = "fromCntToStatus"),
            @Mapping(source = "productItems", target = "productItems"),
            @Mapping(source = "favorite", target = "favorite"),
            @Mapping(target = "tags", source = "tags"),
    })
    public abstract ResponseProduct fromEntity_Items_ToResponse_Detailed(Product product, String[] tags, List<ProductItem> productItems, Boolean favorite);

    public abstract ResponseProductMetadata productToResponseProductMetadata(Product product);

    @Mapping(source = "productDescription", target = "productDescription", ignore = true)
    @Mapping(target = "tags", ignore = true)
    public abstract Product fromRequestUpdateToEntity(RequestProductUpdate request);

    @Mapping(source = "productDescription", target = "productDescription", ignore = true)
    @Mapping(target = "tags", ignore = true)
    public abstract Product fromCreateRequestToEntity(RequestProductCreate request);


    public abstract ProductDescription fromRequestToDescription(RequestProductDescription request);

    @Named("fromCntToStatus")
    public ProductStatus fromCntToStatus(long cnt) {
        return cnt > 0 ? ProductStatus.IN_STOCK : ProductStatus.OUT_OF_STOCK;
    }

    public Specification<Product> fromRequestSearchToSpec(RequestProductSearch searchReq) {
        return ((root, query, criteriaBuilder) -> {
            if (query == null || searchReq == null)
                return criteriaBuilder.conjunction();
            // Prevent N+1 problems with fetch joins
            if (query.getResultType() == Product.class) {
                root.fetch("categories", JoinType.LEFT);
                root.fetch("productDescription", JoinType.LEFT);
            }

            Predicate predicate = criteriaBuilder.equal(root.get("represent"), true);

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
            if (!CollectionUtils.isEmpty(searchReq.tags())) {
                predicate = criteriaBuilder.and(predicate, root.get("tags").get("name").in(searchReq.tags()));
            }

            // Filter deleted products
            if (!searchReq.deleted()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("deletedAt")));
            }

            return predicate;
        });
    }


}