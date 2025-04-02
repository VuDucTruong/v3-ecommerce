package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogCreation;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogSearch;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogUpdate;
import shop.holy.v3.ecommerce.api.dto.blog.ResponseBlog;
import shop.holy.v3.ecommerce.service.biz.BlogService;

@RequiredArgsConstructor
//@Tags({@Tag(name = "Blogs"), @Tag(name = "Blog")})
@Tag(name = "Blogs")
@RestController
@RequestMapping("blogs")
public class ControllerBlog {

    private final BlogService blogService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseBlog createBlog( @ModelAttribute RequestBlogCreation createRequest) {
        ResponseBlog responseBlog = blogService.createBlog(createRequest);
        return responseBlog;
    }

    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseBlog updateBlog(@PathParam("id") String id, @ModelAttribute RequestBlogUpdate updateRequest) {
        ResponseBlog responseBlog = blogService.updateBlog(updateRequest);
        return responseBlog;
    }

    @PostMapping
    public ResponsePagination<ResponseBlog> getBlogs(
            @RequestBody RequestBlogSearch searchRequest) {
        ResponsePagination<ResponseBlog> responsePagination = blogService.search(searchRequest);
        return responsePagination;
    }

    @GetMapping(value = "{id}")
    public ResponseBlog getBlog(@PathParam("id") long id,
                                @RequestParam(name = "deleted", required = false) boolean deleted) {
        return blogService.getBlog(id, deleted);
    }

}