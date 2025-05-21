package shop.holy.v3.ecommerce.api.dto.blog;

import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;

import java.util.Date;
import java.util.List;

public record ResponseBlog(
        long id,
        String title,
        String subtitle,
        ResponseProfile author,
        List<String> genres,
        Date publishedAt,
        String imageUrl,
        String content
) {
}
