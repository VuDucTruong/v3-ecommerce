package shop.holy.v3.ecommerce.shared.mapper;


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

import java.util.Set;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CommentMapper  {

    @Mapping(source = "content", target = "content")
    public abstract Comment fromCreateRequestToEntity(RequestComment requestComment);

    @Mapping(source = "content", target = "content")
    @Mapping(source = "replies", target = "replies", qualifiedByName = "mapReplies")
    @Mapping(source = "author", target = "author")
    public abstract ResponseComment fromEntityToResponse(Comment comment);

    @Mapping(source = "content", target = "content", ignore = true)
    @Mapping(source = "replies", target = "replies", qualifiedByName = "mapReplies_Censored")
    public abstract ResponseComment fromEntityToResponse_Censored(Comment comment);

    @IterableMapping( elementTargetType = ResponseReply.class, qualifiedByName = "toReply")
    @Named("mapReplies")
    public abstract ResponseReply[] mapReplies(Set<Comment> replies);

    @IterableMapping(elementTargetType = ResponseReply.class, qualifiedByName = "toReply_Censored")
    @Named("mapReplies_Censored")
    public abstract ResponseReply[] mapReplies_Censored(Set<Comment> replies);

    @Named("toReply")
    public abstract ResponseReply toResponseReply(Comment reply);

    @Named("toReply_Censored")
    @Mapping(source = "content", target = "content", ignore = true)
    public abstract ResponseReply toResponseReply_censored(Comment reply);


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
            if(StringUtils.hasLength(searchReq.content())){
                criteriaBuilder.and(criteriaBuilder.equal(root.get("content"), searchReq.content()));
            }
//            if (StringUtils.hasLength(searchReq.productName())){
//
//            }

            if (!searchReq.deleted()) {
                criteriaBuilder.and(criteriaBuilder.isNull(root.get("deletedAt")));
            }


            return criteriaBuilder.conjunction();
        };
    }

}
