package shop.holy.v3.ecommerce.shared.mapper;

import jakarta.persistence.criteria.Join;
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
import shop.holy.v3.ecommerce.persistence.entity.Product;
import shop.holy.v3.ecommerce.shared.constant.MappingFunctions;
import shop.holy.v3.ecommerce.shared.util.AppDateUtils;

import java.util.*;

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

    public List<String> fromGenreToStringArray(List<Genre2> genre2s) {
        if (genre2s == null || genre2s.isEmpty())
            return Collections.emptyList();

        List<String> names = new ArrayList<>(genre2s.size() + 1);
        Set<Long> existedG1 = new HashSet<>();
        for (Genre2 genre2 : genre2s) {
            long g1Id = genre2.getGenre1Id();
            names.add(genre2.getName());
            if (existedG1.add(g1Id)) {
                names.add(genre2.getGenre1().getName());
            }
        }
        return names;
    }


    public Specification<Blog> fromSearchRequestToSpec(RequestBlogSearch searchReq) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();
            Join<Blog, Genre2> genre2s = root.join("genre2s");
            genre2s.join("genre1");
            root.fetch("profile");

            if (searchReq.search() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%" + searchReq.search() + "%"));
            }

            ///  TODO: IGNORING GENRES????
            if (!CollectionUtils.isEmpty(searchReq.genreIds())) {
                predicate = criteriaBuilder.and(predicate, genre2s.get("id").in(searchReq.genreIds()));
            }

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
