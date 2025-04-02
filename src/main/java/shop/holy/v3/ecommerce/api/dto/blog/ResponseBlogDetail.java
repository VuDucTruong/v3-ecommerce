package shop.holy.v3.ecommerce.api.dto.blog;

public record ResponseBlogDetail (String title,
                                   String subtitle,
                                   String tags,
                                   String publishedTime,
                                   String image,
                                   String content) {
}
