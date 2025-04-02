package shop.holy.v3.ecommerce.shared.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogCreation;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogSearch;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogUpdate;
import shop.holy.v3.ecommerce.api.dto.blog.ResponseBlog;
import shop.holy.v3.ecommerce.persistence.entity.Blog;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface BlogMapper {

    Blog fromRequestCreateToEntity(RequestBlogCreation request);

    Blog fromRequestUpdateToEntity(RequestBlogUpdate request);

    ResponseBlog fromEntityToResponse(Blog blog);

    default Specification<Blog> fromSearchRequestToSpec(RequestBlogSearch request) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();
            if (request.search() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.like(root.get("title"), "%" + request.search() + "%"));
            }
            if (CollectionUtils.isEmpty(request.tags())) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.like(root.get("author"), "%" + request.search() + "%"));
            }

            if (request.publishedFrom() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.lessThanOrEqualTo(root.get("publishedAt"), request.publishedFrom()));
            }
            if (request.publishedTo() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.greaterThanOrEqualTo(root.get("publishedAt"), request.publishedTo()));
            }

            if (request.deleted()) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.isNotNull(root.get("deletedAt")));
            }
            return predicates;
        };
    }
}
