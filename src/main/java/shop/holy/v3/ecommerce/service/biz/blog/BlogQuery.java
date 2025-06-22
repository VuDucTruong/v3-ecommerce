package shop.holy.v3.ecommerce.service.biz.blog;


import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.blog.RequestBlogSearch;
import shop.holy.v3.ecommerce.api.dto.blog.ResponseBlog;
import shop.holy.v3.ecommerce.api.dto.blog.genre.ResponseGenre1Blogs;
import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;
import shop.holy.v3.ecommerce.persistence.entity.Blog;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_BlogRow_Genre1Id;
import shop.holy.v3.ecommerce.persistence.repository.IBlogRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.mapper.BlogMapper;
import shop.holy.v3.ecommerce.shared.mapper.CommonMapper;
import shop.holy.v3.ecommerce.shared.util.AppDateUtils;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogQuery {

    private final IBlogRepository blogRepository;
    private final BlogMapper blogMapper;
    private final CommonMapper commonMapper;

    public ResponseBlog getBlog(long id, boolean deleted) {

        AuthAccount authAccount = SecurityUtil.getAuthNullable();
        final boolean guessOrCustomer = SecurityUtil.guessOrCustomer(authAccount);
        Specification<Blog> spec = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.equal(root.get("id"), id);
            if (guessOrCustomer || !deleted) {
                predicate = criteriaBuilder.and(predicate, root.get("deletedAt").isNull());
            }
            if (guessOrCustomer) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isNotNull(root.get("approvedAt")));
            }
            return predicate;
        };
        Optional<Blog> blogPost = blogRepository.findOne(spec);
        return blogPost.map(blogMapper::fromEntityToResponse)
                .orElseThrow(BizErrors.BLOG_NOT_FOUND::exception);
    }

    public ResponsePagination<ResponseBlog> search(RequestBlogSearch searchReq) {
        Specification<Blog> spec = blogMapper.fromSearchRequestToSpec(searchReq);
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(searchReq.pageRequest());
        Page<Blog> blogs = blogRepository.findAll(spec, pageable);
        return ResponsePagination.fromPage(blogs.map(blogMapper::fromEntityToResponse));
    }

    public Collection<ResponseGenre1Blogs> findPartitionByGenre1Ids(HashSet<Long> genre1Ids, int size) {
        List<ProQ_BlogRow_Genre1Id> rows = blogRepository.findBlogsLateral(genre1Ids.toArray(Long[]::new), size);
        Collection<ResponseGenre1Blogs> response = rows.stream().map(r -> {
            ResponseProfile responseProfile = new ResponseProfile(r.profileId(), r.fullName(), AppDateUtils.toLocalDate(r.profileCratedAt()), r.profileImageUrlId());
            ResponseBlog responseBlog = new ResponseBlog(r.blogId(), r.title(), r.subtitle(), null, r.approvedAt(), responseProfile, List.of(), r.publishedAt(), commonMapper.genUrl(r.blogImageUrlId()), r.content());
            ArrayList<ResponseBlog> singleItemList = new ArrayList<>(1);
            singleItemList.add(responseBlog);
            return new ResponseGenre1Blogs(r.genre1Id(), singleItemList);
        }).collect(Collectors.toMap(ResponseGenre1Blogs::id, res -> res,
                (v1, v2) -> {
                    v1.blogs().addAll(v2.blogs());
                    return v1;
                })).values();
        return response;
    }


}
