package shop.holy.v3.ecommerce.api.dto.comment;

public record RequestComment(
        long parentCommentId,
        String content
) {

}
