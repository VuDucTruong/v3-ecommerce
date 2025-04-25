package shop.holy.v3.ecommerce.api.dto.blog;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public record RequestBlogCreation(String title,
                                  String subtitle,

                                  Long genreId,
                                  Date publishedAt,
                                  MultipartFile image,
                                  String content
) {
}
