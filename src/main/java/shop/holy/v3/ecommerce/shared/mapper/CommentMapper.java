package shop.holy.v3.ecommerce.shared.mapper;


import org.mapstruct.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import shop.holy.v3.ecommerce.api.dto.comment.RequestComment;
import shop.holy.v3.ecommerce.api.dto.comment.RequestCommentSearch;
import shop.holy.v3.ecommerce.api.dto.comment.ResponseComment;
import shop.holy.v3.ecommerce.api.dto.comment.ResponseReply;
import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;
import shop.holy.v3.ecommerce.persistence.entity.Comment;
import shop.holy.v3.ecommerce.persistence.entity.Profile;

import java.util.Set;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CommentMapper extends IBaseMapper {

    @Mapping(source = "content", target = "content")
    public abstract Comment fromCreateRequestToEntity(RequestComment requestComment);

    @Mapping(source = "content", target = "content")
    @Mapping(source = "replies", target = "replies")
    @Mapping(source = "author", target = "author")
    public abstract ResponseComment fromEntityToResponse(Comment comment);

    @IterableMapping(elementTargetType = ResponseReply.class)
    public abstract ResponseReply[] mapReplies(Set<Comment> replies);

    @Mapping(source = "author", target = "author")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "deletedAt", target = "deletedAt")
    public abstract ResponseReply toResponseReply(Comment reply);

    public abstract ResponseProfile fromProfileToResponseProfile(Profile profile);

    public Specification<Comment> fromSearchRequestToSpec(RequestCommentSearch searchReq) {
        return (root, query, criteriaBuilder) -> {
            if (searchReq == null) return criteriaBuilder.conjunction();

            if (searchReq.productId() != null) {
                criteriaBuilder.and(criteriaBuilder.equal(root.get("productId"), searchReq.productId()));
            }
            if (!CollectionUtils.isEmpty(searchReq.ids())) {
                criteriaBuilder.and(root.get("id").in(searchReq.ids()));
            }
            if (!searchReq.deleted()) {
                criteriaBuilder.and(criteriaBuilder.isNull(root.get("deletedAt")));
            }


            return criteriaBuilder.conjunction();
        };
    }

}
