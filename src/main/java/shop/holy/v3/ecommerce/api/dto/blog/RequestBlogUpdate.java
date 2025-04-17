package shop.holy.v3.ecommerce.api.dto.blog;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;


public record RequestBlogUpdate(long id,
                                String title,
                                String subtitle,
                                Long genreId,
                                Date publishedAt,
                                MultipartFile image,
                                String content
) {
}
