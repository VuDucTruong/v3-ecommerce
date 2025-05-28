package shop.holy.v3.ecommerce.service.biz.blog;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogSearch;
import shop.holy.v3.ecommerce.api.dto.blog.ResponseBlog;
import shop.holy.v3.ecommerce.persistence.entity.Blog;
import shop.holy.v3.ecommerce.persistence.repository.IBlogRepository;
import shop.holy.v3.ecommerce.persistence.repository.IGenre2Repository;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryFacadeService;
import shop.holy.v3.ecommerce.shared.exception.ResourceNotFoundException;
import shop.holy.v3.ecommerce.shared.mapper.BlogMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogQuery {

    private final IBlogRepository blogRepository;
    private final BlogMapper blogMapper;

    public ResponseBlog getBlog(long id, boolean includeDeleted) {
        Optional<Blog> blogPost;
        if (includeDeleted)
            blogPost = blogRepository.findFirstByIdAndDeletedAtIsNull(id);
        else
            blogPost = blogRepository.findById(id);
        return blogPost.map(blogMapper::fromEntityToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found by id " + id));
    }

    public ResponsePagination<ResponseBlog> search(RequestBlogSearch searchReq) {
        Specification<Blog> spec = blogMapper.fromSearchRequestToSpec(searchReq);
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(searchReq.pageRequest());
        Page<Blog> blogs = blogRepository.findAll(spec, pageable);
        return ResponsePagination.fromPage(blogs.map(blogMapper::fromEntityToResponse));
    }

}
