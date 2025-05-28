package shop.holy.v3.ecommerce.service.biz.blog;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogCreation;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogUpdate;
import shop.holy.v3.ecommerce.api.dto.blog.ResponseBlog;
import shop.holy.v3.ecommerce.persistence.entity.Blog;
import shop.holy.v3.ecommerce.persistence.entity.Genre2;
import shop.holy.v3.ecommerce.persistence.repository.IBlogRepository;
import shop.holy.v3.ecommerce.persistence.repository.IGenre2Repository;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryFacadeService;
import shop.holy.v3.ecommerce.shared.exception.BadRequestException;
import shop.holy.v3.ecommerce.shared.mapper.BlogMapper;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogCommand {

    private final IBlogRepository blogRepository;
    private final BlogMapper blogMapper;
    private final CloudinaryFacadeService cloudinaryService;
    private final IGenre2Repository genre2Repository;

    @Transactional
    public ResponseBlog createBlog(RequestBlogCreation request) {
        long profileId = SecurityUtil.getAuthProfileId();
        Blog blogPost = blogMapper.fromRequestCreateToEntity(request);
        blogPost.setProfileId(profileId);
        Blog rs = upsertAndReturnChanges(blogPost, request.image(), false);
        var genre2s = insertAndFetchGenre2s(blogPost.getId(), request.genreIds(), false);
        rs.setGenre2s(genre2s);
        return blogMapper.fromEntityToResponse(rs);
    }

    @Transactional
    public ResponseBlog updateBlog(RequestBlogUpdate request) {
        long profileId = SecurityUtil.getAuthProfileId();
        Blog blogPost = blogMapper.fromRequestUpdateToEntity(request);
        blogPost.setProfileId(profileId);

        Blog rs = upsertAndReturnChanges(blogPost, request.image(), true);
        var genre2s = insertAndFetchGenre2s(request.id(), request.genreIds(), true);
        rs.setGenre2s(genre2s);
        return blogMapper.fromEntityToResponse(rs);
    }

    private List<Genre2> insertAndFetchGenre2s(long blogId, @Valid @NotEmpty(message = "genres must not be empty") Collection<Long> genre2Ids, boolean update) {
        if (update) genre2Repository.deleteBlogsGenres(blogId);
        for (long genreId : genre2Ids) {
            genre2Repository.insertBlogsGenres(blogId, genreId);
        }
        return genre2Repository.findByIdIn(genre2Ids);
    }

    @Transactional
    public int deleteBlog(long id) {
        return blogRepository.updateBlogDeletedAtById(id);
    }

    @Transactional
    public int deleteBlogs(long[] ids) {
        if (ids == null || ids.length == 0)
            return blogRepository.updateBlogDeletedAtByIdIn(ids);
        return 0;
    }


    private Blog upsertAndReturnChanges(Blog blogPost, MultipartFile image, boolean isUpdate) throws BadRequestException {
        Blog rs = blogPost;
        if (image == null || image.isEmpty()) {
            if (isUpdate)
                blogRepository.updateBlogIfNotNull(blogPost, blogPost.getId(), blogPost.getProfileId());
            else
                rs = blogRepository.save(blogPost);
            return rs;
        }
        /// IMAGE IS NOT NULL
        String imageUrl = cloudinaryService.uploadBlogBlob(image);
        blogPost.setImageUrlId(imageUrl);
        if (isUpdate) {
            blogRepository.updateBlogIfNotNull(blogPost, blogPost.getId(), blogPost.getProfileId());
        } else
            rs = blogRepository.save(blogPost);
        /// IMAGE URL WILL BE OVERRIDDEN by the file image if set. or take imageUrl by default

        return rs;
    }


}
