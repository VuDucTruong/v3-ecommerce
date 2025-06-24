package shop.holy.v3.ecommerce.shared.mapper;


import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.mapstruct.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.comment.RequestComment;
import shop.holy.v3.ecommerce.api.dto.comment.RequestCommentSearch;
import shop.holy.v3.ecommerce.api.dto.comment.ResponseComment;
import shop.holy.v3.ecommerce.api.dto.comment.ResponseReply;
import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;
import shop.holy.v3.ecommerce.persistence.entity.Comment;
import shop.holy.v3.ecommerce.persistence.entity.Profile;
import shop.holy.v3.ecommerce.shared.util.SqlUtils;

import java.util.Set;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CommentMapper {

    @Mapping(source = "content", target = "content")
    public abstract Comment fromCreateRequestToEntity(RequestComment requestComment);

    @Mapping(source = "content", target = "content")
    @Mapping(source = "replies", target = "replies", qualifiedByName = "mapReplies")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "author.account.role", target = "role")
    public abstract ResponseComment fromEntityToResponse(Comment comment);

    @Mapping(source = "content", target = "content")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "author.account.role", target = "role")
    public abstract ResponseComment.Flattened fromEntityToResponseFlattened(Comment comment);


    @Mapping(source = "content", target = "content")
    @Mapping(source = "replies", target = "replies", qualifiedByName = "mapReplies")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "author.account.role", target = "role")
    public abstract ResponseComment.Light fromEntityToResponseLight(Comment comment);

    @IterableMapping(elementTargetType = ResponseReply.class, qualifiedByName = "toReply")
    @Named("mapReplies")
    public abstract ResponseReply[] mapReplies(Set<Comment> replies);

    @Named("toReply")
    @Mapping(source = "author.account.role", target = "role")
    public abstract ResponseReply toResponseReply(Comment reply);

    public abstract ResponseProfile fromProfileToResponseProfile(Profile profile);

    public Specification<Comment> fromSearchRequestToSpec(RequestCommentSearch searchReq) {
        return (root, query, criteriaBuilder) -> {
            if (query == null || searchReq == null)
                return criteriaBuilder.conjunction();
            if (!query.getResultType().equals(Long.class)) {
                root.fetch("product", JoinType.INNER);
                root.fetch("author", JoinType.INNER);
                root.fetch("parent", JoinType.LEFT);
            }

            Predicate predicate = criteriaBuilder.conjunction();

            if (!CollectionUtils.isEmpty(searchReq.ids())) {
                var searchParentContent = root.get("id").in(searchReq.ids());
//                var searchRepliesContent = parentGet.get("id").in(searchReq.ids());
//                var idInSearch = criteriaBuilder.or(searchParentContent, searchRepliesContent);

                predicate = criteriaBuilder.and(predicate, searchParentContent);
            }

            if (StringUtils.hasLength(searchReq.search())) {
                var searchParentContent = SqlUtils.likeIgnoreCase(criteriaBuilder, root.get("content"), searchReq.search());
//                var searchRepliesContent = criteriaBuilder.like(parentGet.get("content"), "%" + searchReq.search().toLowerCase() + "%");
//                var contentSearch = criteriaBuilder.or(searchParentContent, searchRepliesContent);
                predicate = criteriaBuilder.and(predicate, searchParentContent);
            }

            if (StringUtils.hasLength(searchReq.productName())) {
                var searchParentProduct = SqlUtils.likeIgnoreCase(criteriaBuilder, root.get("product").get("name"), searchReq.productName());
//                var searchRepliesProduct = criteriaBuilder.like(parentGet.get("product").get("name"), "%" + searchReq.productName().toLowerCase() + "%");
//                var productSearch = criteriaBuilder.or(searchParentProduct, searchRepliesProduct);
                predicate = criteriaBuilder.and(predicate, searchParentProduct);
            }
            if (!searchReq.deleted()) {
                var searchParentDeletedAt = criteriaBuilder.isNull(root.get("deletedAt"));
//                var searchDeletedAt = criteriaBuilder.and(searchParentDeletedAt, searchRepliesDeletedAt);
                predicate = criteriaBuilder.and(predicate, searchParentDeletedAt);
            }

            return predicate;
        };
    }

}
