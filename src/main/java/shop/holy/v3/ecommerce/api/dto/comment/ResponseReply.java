package shop.holy.v3.ecommerce.api.dto.comment;

import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;

import java.util.Date;

public record ResponseReply(ResponseProfile author,
                            Date createdAt,
                            Date deletedAt,
                            String content) {

}