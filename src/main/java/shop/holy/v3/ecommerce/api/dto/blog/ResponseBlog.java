package shop.holy.v3.ecommerce.api.dto.blog;


import java.util.Date;

public record ResponseBlog(String title,
                           String subtitle,
                           String[] genres,
                           Date publishedAt,
                           String imageUrl,
                           String content) {
}
