package shop.holy.v3.ecommerce.api.dto.blog;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public record RequestBlogCreation(String title,
                                  String subtitle,
                                  List<String> tags,
                                  Date publishedTime,
                                  MultipartFile image,
                                  String content) {
}
