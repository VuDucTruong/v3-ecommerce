package shop.holy.v3.ecommerce.api.dto.comment;

import shop.holy.v3.ecommerce.api.dto.product.ResponseProductMetadata;
import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;

import java.util.Date;

public record ResponseComment(
        long id,
        ResponseProductMetadata product,
        ResponseProfile author,
        Date createdAt,
        Date deletedAt,
        String content,
        ResponseReply[] replies
) {
    public record Light(
            long id,
            ResponseProfile author,
            Date createdAt,
            Date deletedAt,
            String content,
            ResponseReply[] replies
    ){}

}
