package shop.holy.v3.ecommerce.api.dto.comment;

import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;

import java.util.Date;

public record ResponseReply(long id,
                            ResponseProfile author,
                            String role,
                            Long parentCommentId,
                            Date createdAt,
                            Date deletedAt,
                            String content) {

}