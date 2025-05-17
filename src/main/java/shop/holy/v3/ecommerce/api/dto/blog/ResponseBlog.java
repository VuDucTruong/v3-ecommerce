package shop.holy.v3.ecommerce.api.dto.blog;

import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;

import java.util.Date;

public record ResponseBlog(
        long id,
        String title,
        String subtitle,
        ResponseProfile author,
        String[] genres,
        Date publishedAt,
        String imageUrl,
        String content
) {
}
