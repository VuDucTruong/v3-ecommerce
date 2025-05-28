package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogCreation;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogSearch;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogUpdate;
import shop.holy.v3.ecommerce.api.dto.blog.ResponseBlog;
import shop.holy.v3.ecommerce.service.biz.blog.BlogCommand;
import shop.holy.v3.ecommerce.service.biz.blog.BlogQuery;

@RequiredArgsConstructor
//@Tags({@Tag(name = "Blogs"), @Tag(name = "Blog")})
@Tag(name = "Blogs")
@RestController
@RequestMapping("blogs")
public class ControllerBlog {

    private final BlogCommand blogCommand;
    private final BlogQuery blogQuery;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_STAFF)")
    @Operation(summary = "create 1")
    public ResponseBlog createBlog(@ModelAttribute RequestBlogCreation createRequest) {
        ResponseBlog responseBlog = blogCommand.createBlog(createRequest);
        return responseBlog;
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_STAFF)")
    @Operation(summary = "update 1")
    public ResponseBlog updateBlog(@ModelAttribute RequestBlogUpdate updateRequest) {

        ResponseBlog responseBlog = blogCommand.updateBlog(updateRequest);
        return responseBlog;
    }

    @PostMapping(value = "searches")
    @Operation(description = """
             1. hiện là cái genres chỉ search trong genre1 \n
            ===> where .... AND genre1.name IN (list<genres) 
            """)
    public ResponsePagination<ResponseBlog> getBlogs(
            @RequestBody RequestBlogSearch searchRequest) {
        ResponsePagination<ResponseBlog> responsePagination = blogQuery.search(searchRequest);
        return responsePagination;
    }


    @GetMapping(value = "{id}")
    @Operation(summary = "get 1")
    public ResponseBlog getBlog(@PathVariable("id") long id,
                                @RequestParam(name = "deleted", required = false) boolean deleted) {
        return blogQuery.getBlog(id, deleted);
    }

    @DeleteMapping(value = "{id}")
    @Operation(summary = "delete 1")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_STAFF)")
    public int deleteBlog(@PathVariable("id") long id) {
        return blogCommand.deleteBlog(id);
    }


    @DeleteMapping(value = "")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    @Operation(summary = "delete many")
    public int deleteBlogs(@RequestParam long ids[]) {
        return blogCommand.deleteBlogs(ids);
    }

}