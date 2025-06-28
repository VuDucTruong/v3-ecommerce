package shop.holy.v3.ecommerce.shared.mapper;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogCreation;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogSearch;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogUpdate;
import shop.holy.v3.ecommerce.api.dto.blog.ResponseBlog;
import shop.holy.v3.ecommerce.persistence.entity.Blog;
import shop.holy.v3.ecommerce.persistence.entity.Genre2;
import shop.holy.v3.ecommerce.shared.constant.MapFuncs;
import shop.holy.v3.ecommerce.shared.util.AppDateUtils;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;
import shop.holy.v3.ecommerce.shared.util.SqlUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = CommonMapper.class)
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public abstract class BlogMapper {

    public abstract Blog fromRequestCreateToEntity(RequestBlogCreation request);

    @Mapping(source = "imageUrl", target = "imageUrlId", qualifiedByName = MapFuncs.EXTRACT_BLOG_PUBLIC_ID)
    public abstract Blog fromRequestUpdateToEntity(RequestBlogUpdate request);

    @Mapping(source = "imageUrlId", target = "imageUrl", qualifiedByName = MapFuncs.GEN_URL)
    @Mapping(source = "genre2s", target = "genre2Ids", qualifiedByName = "fromGenre2EntityToIds")
    @Mapping(source = "profile", target = "author")
    public abstract ResponseBlog fromEntityToResponse(Blog blog);


    @Named("fromGenre2EntityToIds")
    public List<Long> fromGenre2EntityToIds(List<Genre2> genre2s) {
        if (CollectionUtils.isEmpty(genre2s)) {
            return Collections.emptyList();
        }
        return genre2s.stream().map(Genre2::getId).collect(Collectors.toList());
    }

    public Specification<Blog> fromSearchRequestToSpec(RequestBlogSearch searchReq) {
        return (root, query, criteriaBuilder) -> {
            if (query == null || searchReq == null)
                return criteriaBuilder.conjunction();

            var predicate = criteriaBuilder.conjunction();
            if (!query.getResultType().equals(Long.class)) {
                var genre2sJoin = root.fetch("genre2s", JoinType.LEFT);
                genre2sJoin.fetch("genre1", JoinType.LEFT);
                root.fetch("profile", JoinType.LEFT);
            }

            if (searchReq.search() != null) {
                predicate = criteriaBuilder.and(predicate, SqlUtils.likeIgnoreCase(criteriaBuilder, root.get("title"), searchReq.search()));
            }

            if (!CollectionUtils.isEmpty(searchReq.genreIds())) {
                predicate = criteriaBuilder.and(predicate, root.get("genre2s").get("id").in(searchReq.genreIds()));
            }

            if (searchReq.publishedFrom() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("publishedAt"), AppDateUtils.toStartOfDay(searchReq.publishedFrom())));
            }
            if (searchReq.publishedTo() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("publishedAt"), AppDateUtils.toEndOfDay(searchReq.publishedTo())));
            }


            boolean guessOrCustomer = SecurityUtil.guessOrCustomer();
            if (guessOrCustomer || !searchReq.deleted()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNull(root.get("deletedAt")));
            }

            if (guessOrCustomer || searchReq.approved()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNotNull(root.get("approvedAt")));
            }

            return predicate;
        };
    }
}
