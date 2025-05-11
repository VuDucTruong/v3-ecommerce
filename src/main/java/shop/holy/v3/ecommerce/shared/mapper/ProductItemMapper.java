package shop.holy.v3.ecommerce.shared.mapper;


import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.tuple.Triple;
import org.mapstruct.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.product.item.*;
import shop.holy.v3.ecommerce.persistence.entity.Product;
import shop.holy.v3.ecommerce.persistence.entity.ProductItem;
import shop.holy.v3.ecommerce.persistence.entity.ProductItemUsed;
import shop.holy.v3.ecommerce.shared.constant.MappingFunctions;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
uses = {CommonMapper.class})
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductItemMapper {
    @Mapping(target = "used", constant = "false")
    public abstract ResponseProductItem fromEntityToResponse(ProductItem product);

    public abstract ProductItem fromRequestToEntity(RequestProductItemCreate productItem);

    public abstract ProductItem fromUpdateRequestToEntity(RequestProductItemUpdate productItem);

    @Mapping(target = "used", constant = "true")
    public abstract ResponseProductItem fromUsedEntityToResponse(ProductItemUsed usedProductItem);


    @Mapping(target = "slug", source = "product.slug")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "imageUrl", source = "product.imageUrlId", qualifiedByName = MappingFunctions.GEN_URL)
    @Mapping(target = "represent", source = "product.represent")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "originalPrice", source = "product.originalPrice")
    @Mapping(target = "productId", source = "item.productId")
    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "productKey", source = "item.productKey")
    @Mapping(target = "createdAt", source = "item.createdAt")
    @Mapping(target = "region", source = "item.region")
    @Mapping(target = "used", source = "used")
    public abstract ResponseProductItems_Indetails from_EntityToResponse_Indetails(
            ProductItem item, Product product, boolean used);

    @Mapping(target = "slug", source = "product.slug")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "imageUrl", source = "product.imageUrlId", qualifiedByName = MappingFunctions.GEN_URL)
    @Mapping(target = "represent", source = "product.represent")
    @Mapping(target = "price", source = "product.price")
    @Mapping(target = "originalPrice", source = "product.originalPrice")
    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "productId", source = "item.productId")
    @Mapping(target = "productKey", source = "item.productKey")
    @Mapping(target = "createdAt", source = "item.createdAt")
    @Mapping(target = "region", source = "item.region")
    @Mapping(target = "used", source = "used")
    public abstract ResponseProductItems_Indetails fromused_EntityToResponse_Indetails(
            ProductItemUsed item, Product product, boolean used);

    public abstract ProductItemUsed from_NonUsedEntity_ToUsedEntity(ProductItem productItem);

    public abstract ProductItemUsed from_Request_ToUsedEntity(RequestProductItemCreate productItem);


    public Triple<long[], String[], String[]> from_UsedEntity_To_Tri_Arrays(ProductItemUsed[] usedProductItem) {
        long[] prod_ids = new long[usedProductItem.length];
        String[] prod_keys = new String[usedProductItem.length];
        String[] regions = new String[usedProductItem.length];
        for (int i = 0; i < usedProductItem.length; i++) {
            prod_ids[i] = usedProductItem[i].getProductId();
            prod_keys[i] = usedProductItem[i].getProductKey();
            regions[i] = usedProductItem[i].getRegion();
        }
        return Triple.of(prod_ids, prod_keys, regions);
    }

    public Triple<long[], String[], String[]> from_Entity_To_Tri_Arrays(ProductItem[] productItem) {
        long[] prod_ids = new long[productItem.length];
        String[] prod_keys = new String[productItem.length];
        String[] regions = new String[productItem.length];
        for (int i = 0; i < productItem.length; i++) {
            prod_ids[i] = productItem[i].getProductId();
            prod_keys[i] = productItem[i].getProductKey();
            regions[i] = productItem[i].getRegion();
        }
        return Triple.of(prod_ids, prod_keys, regions);
    }


    public <T> Specification<T> fromRequestSearchToSpec(RequestProductItemSearch searchReq) {
        return (root, query, criteriaBuilder) -> {
            assert query != null;
            if (searchReq == null) return criteriaBuilder.conjunction();

            Predicate predicate = criteriaBuilder.conjunction();

            if (searchReq.productId() != null) {
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


            return predicate;
        };
    }

}
