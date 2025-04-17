package shop.holy.v3.ecommerce.api.dto.comment;

import java.util.List;

public record ResponseComment(
        String content,
        List<Reply> repiles
) {
    public record Reply(String content) {

    }
}
