package shop.holy.v3.ecommerce.shared.mapper;

import jakarta.persistence.criteria.Predicate;
import org.mapstruct.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategoryCreate;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategorySearch;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategoryUpdate;
import shop.holy.v3.ecommerce.api.dto.category.ResponseCategory;
import shop.holy.v3.ecommerce.persistence.entity.Category;
import shop.holy.v3.ecommerce.shared.constant.MappingFunctions;

@Mapper(componentModel = "spring",
uses = CommonMapper.class)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CategoryMapper {

    public abstract Category fromCreateRequestToEntity(RequestCategoryCreate categoryCreateRequest);

    public abstract Category fromUpdateRequestToEntity(RequestCategoryUpdate categoryUpdateRequest);

    @Mapping(target = "imageUrl", source = "imageUrlId", qualifiedByName = MappingFunctions.GEN_URL)
    public abstract ResponseCategory fromEntityToResponse(Category category);

    public abstract Category fromResponseToEntity(ResponseCategory categoryResponse);

    public Specification<Category> fromRequestSearchToSpec(RequestCategorySearch searchReq) {
        return ((root, query, criteriaBuilder) -> {
            if (searchReq == null) return criteriaBuilder.conjunction();
            Predicate predicate = criteriaBuilder.conjunction();
            if (StringUtils.hasLength(searchReq.search())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + searchReq.search().toLowerCase() + "%"));
            }
            if(!CollectionUtils.isEmpty(searchReq.ids())){
                predicate = criteriaBuilder.and(predicate, root.get("id").in(searchReq.ids()));
            }

            if (!searchReq.deleted()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("deletedAt")));
            }
            return predicate;
        });
    }
}
