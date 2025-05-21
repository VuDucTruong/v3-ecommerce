package shop.holy.v3.ecommerce.shared.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogCreation;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogSearch;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogUpdate;
import shop.holy.v3.ecommerce.api.dto.blog.ResponseBlog;
import shop.holy.v3.ecommerce.persistence.entity.Blog;
import shop.holy.v3.ecommerce.persistence.entity.Genre2;
import shop.holy.v3.ecommerce.shared.constant.MappingFunctions;
import shop.holy.v3.ecommerce.shared.util.AppDateUtils;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = CommonMapper.class)
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public abstract class BlogMapper {

    public abstract Blog fromRequestCreateToEntity(RequestBlogCreation request);

    public abstract Blog fromRequestUpdateToEntity(RequestBlogUpdate request);

    @Mapping(source = "imageUrlId", target = "imageUrl", qualifiedByName = MappingFunctions.GEN_URL)
    @Mapping(source = "genre2s", target = "genres")
    @Mapping(source = "profile", target = "author")
    public abstract ResponseBlog fromEntityToResponse(Blog blog);

    public String[] fromGenreToStringArray(List<Genre2> genre2s) {
        if (genre2s == null || genre2s.isEmpty())
            return new String[0];
        return genre2s.stream().map(Genre2::getName)
                .toArray(String[]::new);
    }


    public Specification<Blog> fromSearchRequestToSpec(RequestBlogSearch searchReq) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();
            root.fetch("genre2s");
            root.fetch("profile");

            if (searchReq.search() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%" + searchReq.search() + "%"));
            }

            ///  TODO: IGNORING GENRES????
//            if (!CollectionUtils.isEmpty(searchReq.genres())) {
//                predicate = criteriaBuilder.and(predicate, root.get("genre1").get("name").in(searchReq.genres()));
//            }

            if (searchReq.publishedFrom() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("publishedAt"), AppDateUtils.toStartOfDay(searchReq.publishedFrom())));
            }
            if (searchReq.publishedTo() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("publishedAt"), AppDateUtils.toEndOfDay(searchReq.publishedTo())));
            }

            if (!searchReq.deleted()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("deletedAt")));
            }
            return predicate;
        };
    }
}
