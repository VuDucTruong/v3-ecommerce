package shop.holy.v3.ecommerce.shared.mapper;

import jakarta.persistence.criteria.Predicate;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategoryCreate;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategorySearch;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategoryUpdate;
import shop.holy.v3.ecommerce.api.dto.category.ResponseCategory;
import shop.holy.v3.ecommerce.persistence.entity.Category;
import shop.holy.v3.ecommerce.shared.constant.MapFuncs;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;
import shop.holy.v3.ecommerce.shared.util.SqlUtils;

@Mapper(componentModel = "spring",
        uses = CommonMapper.class)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CategoryMapper {

    public abstract Category fromCreateRequestToEntity(RequestCategoryCreate categoryCreateRequest);

    //    @Mapping(source = "imageUrl", target = "imageUrlId", qualifiedByName = MapFuncs.EXTRACT_CATEGORY_PUBLIC_ID)
    public abstract Category fromUpdateRequestToEntity(RequestCategoryUpdate categoryUpdateRequest);

    @Mapping(target = "imageUrl", source = "imageUrlId", qualifiedByName = MapFuncs.GEN_URL)
    public abstract ResponseCategory fromEntityToResponse(Category category);


    public Specification<Category> fromRequestSearchToSpec(RequestCategorySearch searchReq) {
        return ((root, query, criteriaBuilder) -> {
            if (searchReq == null) return criteriaBuilder.conjunction();
            Predicate predicate = criteriaBuilder.conjunction();
            if (StringUtils.hasLength(searchReq.search())) {
                predicate = criteriaBuilder.and(predicate, SqlUtils.likeIgnoreCase(criteriaBuilder, root.get("name"), searchReq.search()));
            }
            if (!CollectionUtils.isEmpty(searchReq.ids())) {
                predicate = criteriaBuilder.and(predicate, root.get("id").in(searchReq.ids()));
            }

            if (SecurityUtil.guessOrCustomer() || !searchReq.deleted()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("deletedAt")));
            }

            return predicate;
        });
    }
}
