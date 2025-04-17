package shop.holy.v3.ecommerce.shared.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;
import shop.holy.v3.ecommerce.api.dto.comment.RequestComment;
import shop.holy.v3.ecommerce.api.dto.comment.ResponseComment;
import shop.holy.v3.ecommerce.persistence.entity.Comment;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CommentMapper extends IBaseMapper {

    public abstract Comment fromCreateRequestToEntity(RequestComment requestComment);

    public abstract ResponseComment fromEntityToResponse(Comment comment);


}
