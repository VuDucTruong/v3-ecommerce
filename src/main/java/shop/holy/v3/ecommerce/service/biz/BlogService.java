package shop.holy.v3.ecommerce.service.biz;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogCreation;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogSearch;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogUpdate;
import shop.holy.v3.ecommerce.api.dto.blog.ResponseBlog;
import shop.holy.v3.ecommerce.persistence.entity.Blog;
import shop.holy.v3.ecommerce.persistence.repository.IBlogRepository;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryFacadeService;
import shop.holy.v3.ecommerce.shared.exception.BadRequestException;
import shop.holy.v3.ecommerce.shared.exception.ResourceNotFoundException;
import shop.holy.v3.ecommerce.shared.mapper.BlogMapper;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final IBlogRepository blogRepository;
    private final BlogMapper blogMapper;
    private final CloudinaryFacadeService cloudinaryService;


    public ResponseBlog createBlog(RequestBlogCreation request) {
        Blog blogPost = blogMapper.fromRequestCreateToEntity(request);
        return upsertAndReturnChanges(blogPost, request.image());
    }

    public ResponseBlog getBlog(long id, boolean includeDeleted) {
        Optional<Blog> blogPost;
        if (includeDeleted)
            blogPost = blogRepository.findFirstByIdAndDeletedAtIsNull(id);
        else
            blogPost = blogRepository.findById(id);
        return blogPost.map(blogMapper::fromEntityToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Blog post not found by id " + id));
    }

    @Transactional
    public int deleteBlog(long id) {
        return blogRepository.updateBlogDeletedAt(id);
    }

    @Transactional
    public ResponseBlog updateBlog(RequestBlogUpdate request) {
        Blog blogPost = blogMapper.fromRequestUpdateToEntity(request);
        return upsertAndReturnChanges(blogPost, request.image());
    }

    private ResponseBlog upsertAndReturnChanges(Blog blogPost, MultipartFile image) throws BadRequestException {
        try {
            if (image != null) {
                String imageUrl = cloudinaryService.uploadBlogBlob(image);
                blogPost.setImageUrlId(imageUrl);
            }
        } catch (IOException e) {
            throw new BadRequestException("Failed to upload image");
        }
        Blog result = blogRepository.save(blogPost);
        return blogMapper.fromEntityToResponse(result);
    }

    public ResponsePagination<ResponseBlog> search(RequestBlogSearch searchReq) {
        Specification<Blog> spec = blogMapper.fromSearchRequestToSpec(searchReq);
        Pageable pageable = blogMapper.fromRequestPageableToPageable(searchReq.pageRequest());
        Page<Blog> blogs = blogRepository.findAll(spec, pageable);
        return ResponsePagination.fromPage(blogs.map(blogMapper::fromEntityToResponse));
    }

}
