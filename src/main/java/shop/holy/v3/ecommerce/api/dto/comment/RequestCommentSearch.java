package shop.holy.v3.ecommerce.api.dto.comment;

import java.util.Set;

public record RequestCommentSearch(
        Set<Long> ids,
        boolean deleted
) {
}
