package shop.holy.v3.ecommerce.api.dto.blog.genre;

import shop.holy.v3.ecommerce.api.dto.blog.ResponseBlog;

import java.util.List;

public record ResponseGenre1Blogs(
        long id,
        List<ResponseBlog> blogs
) {


}
