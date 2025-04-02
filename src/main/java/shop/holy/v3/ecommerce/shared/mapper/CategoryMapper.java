package shop.holy.v3.ecommerce.shared.mapper;

import jakarta.persistence.criteria.Predicate;
import org.mapstruct.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategoryCreate;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategorySearch;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategoryUpdate;
import shop.holy.v3.ecommerce.api.dto.category.ResponseCategory;
import shop.holy.v3.ecommerce.persistence.entity.Category;

@Mapper(componentModel = "spring")
@MapperConfig(
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CategoryMapper extends IBaseMapper {

    public abstract Category fromCreateRequestToEntity(RequestCategoryCreate categoryCreateRequest);

    public abstract Category fromUpdateRequestToEntity(RequestCategoryUpdate categoryUpdateRequest);

    @Mapping(target = "imageUrl", source = "imageUrlId", qualifiedByName = "genUrl")
    public abstract ResponseCategory fromEntityToResponse(Category category);

    public abstract Category fromResponseToEntity(ResponseCategory categoryResponse);

    public Specification<Category> fromRequestSearchToSpec(RequestCategorySearch searchReq) {
        return ((root, query, criteriaBuilder) -> {
            if (searchReq == null) return criteriaBuilder.conjunction();
            Predicate predicate = criteriaBuilder.conjunction();
            if (StringUtils.hasLength(searchReq.search())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("name"), "%" + searchReq.search().toLowerCase() + "%"));
            }
            return predicate;
        });
    }
}
