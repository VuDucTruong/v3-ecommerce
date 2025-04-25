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
import shop.holy.v3.ecommerce.persistence.entity.Genre1;
import shop.holy.v3.ecommerce.persistence.entity.Genre2;
import shop.holy.v3.ecommerce.shared.constant.MappingFunctions;

import java.util.Set;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public abstract class BlogMapper extends IBaseMapper {

    public abstract Blog fromRequestCreateToEntity(RequestBlogCreation request);

    public abstract Blog fromRequestUpdateToEntity(RequestBlogUpdate request);

    @Mapping(source = "imageUrlId", target = "imageUrl", qualifiedByName = MappingFunctions.GEN_URL)
    public abstract ResponseBlog fromEntityToResponse(Blog blog);

    public String[] fromGenreToStringArray(Set<Genre1> genre1s) {
        return genre1s.stream()
                .flatMap(genre1 -> {
                    Stream<String> genreName = Stream.of(genre1.getName());
                    Stream<String> genre2Names = genre1.getGenre2s() != null ?
                            genre1.getGenre2s().stream().map(Genre2::getName) :
                            Stream.empty();
                    return Stream.concat(genreName, genre2Names);
                })
                .toArray(String[]::new);
    }



    public Specification<Blog> fromSearchRequestToSpec(RequestBlogSearch searchReq) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();
            root.fetch("genres");

            if (searchReq.search() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%" + searchReq.search() + "%"));
            }
            if (CollectionUtils.isEmpty(searchReq.genres())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("author"), "%" + searchReq.search() + "%"));
            }

            if (searchReq.publishedFrom() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("publishedAt"), searchReq.publishedFrom()));
            }
            if (searchReq.publishedTo() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("publishedAt"), searchReq.publishedTo()));
            }

            if (!searchReq.deleted()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("deletedAt")));
            }
            return predicate;
        };
    }
}
