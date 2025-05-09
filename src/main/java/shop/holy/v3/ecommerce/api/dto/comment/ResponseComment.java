package shop.holy.v3.ecommerce.api.dto.comment;

import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;

import java.util.Date;

public record ResponseComment(
        long id,
        ResponseProfile author,
        Date createdAt,
        Date deletedAt,
        String content,
        ResponseReply[] replies
) {

}
