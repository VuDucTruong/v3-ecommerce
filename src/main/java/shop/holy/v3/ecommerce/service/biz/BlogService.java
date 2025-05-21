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
import shop.holy.v3.ecommerce.persistence.repository.IGenre2Repository;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryFacadeService;
import shop.holy.v3.ecommerce.shared.exception.BadRequestException;
import shop.holy.v3.ecommerce.shared.exception.ResourceNotFoundException;
import shop.holy.v3.ecommerce.shared.mapper.BlogMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final IBlogRepository blogRepository;
    private final BlogMapper blogMapper;
    private final CloudinaryFacadeService cloudinaryService;
    private final IGenre2Repository genre2Repository;

    public ResponseBlog createBlog(RequestBlogCreation request) {
        long profileId = SecurityUtil.getAuthProfileId();
        Blog blogPost = blogMapper.fromRequestCreateToEntity(request);
        blogPost.setProfileId(profileId);
        return upsertAndReturnChanges(blogPost, request.image(), false);
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
        return blogRepository.updateBlogDeletedAtById(id);
    }

    @Transactional
    public int deleteBlogs(long[] ids) {
        if (ids == null && ids.length > 0)
            return blogRepository.updateBlogDeletedAtByIdIn(ids);
        return 0;
    }

    @Transactional
    public ResponseBlog updateBlog(RequestBlogUpdate request) {
        long profileId = SecurityUtil.getAuthProfileId();
        Blog blogPost = blogMapper.fromRequestUpdateToEntity(request);
        blogPost.setProfileId(profileId);

        return upsertAndReturnChanges(blogPost, request.image(), true);
    }

    private ResponseBlog upsertAndReturnChanges(Blog blogPost, MultipartFile image, boolean isUpdate) throws BadRequestException {
        if (image != null) {
            String imageUrl = cloudinaryService.uploadBlogBlob(image);
            blogPost.setImageUrlId(imageUrl);
        }
        if (isUpdate) {
            blogRepository.updateBlogIfNotNull(blogPost, blogPost.getId(), blogPost.getProfileId());
        } else
            blogRepository.save(blogPost);

        return blogMapper.fromEntityToResponse(blogPost);
    }

    public ResponsePagination<ResponseBlog> search(RequestBlogSearch searchReq) {
        Specification<Blog> spec = blogMapper.fromSearchRequestToSpec(searchReq);
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(searchReq.pageRequest());
        Page<Blog> blogs = blogRepository.findAll(spec, pageable);
        return ResponsePagination.fromPage(blogs.map(blogMapper::fromEntityToResponse));
    }

}
