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
        List<ProQ_BlogRow_Genre1Id> rows = blogRepository.findBlogsLateralNotDeleted(genre1Ids.toArray(Long[]::new), size);

        // Group by genre1Id -> blogId
        Map<Long, Map<Long, List<ProQ_BlogRow_Genre1Id>>> grouped = rows.stream()
                .collect(Collectors.groupingBy(ProQ_BlogRow_Genre1Id::genre1Id,
                        Collectors.groupingBy(ProQ_BlogRow_Genre1Id::blogId)));

        Collection<ResponseGenre1Blogs> response = grouped.entrySet().stream().map(genreEntry -> {
            long genre1Id = genreEntry.getKey();

            List<ResponseBlog> blogs = genreEntry.getValue().entrySet().stream().map(blogEntry -> {
                List<ProQ_BlogRow_Genre1Id> blogRows = blogEntry.getValue();
                ProQ_BlogRow_Genre1Id sample = blogRows.getFirst(); // all same blog

                List<Long> g2Ids = blogRows.stream()
                        .map(ProQ_BlogRow_Genre1Id::g2Id)
                        .distinct()
                        .toList();

                ResponseProfile responseProfile = new ResponseProfile(
                        sample.profileId(),
                        sample.fullName(),
                        AppDateUtils.toLocalDate(sample.profileCratedAt()),
                        sample.profileImageUrlId());

                return new ResponseBlog(
                        sample.blogId(),
                        sample.title(),
                        sample.subtitle(),
                        null,
                        sample.approvedAt(),
                        responseProfile,
                        g2Ids,
                        sample.publishedAt(),
                        commonMapper.genUrl(sample.blogImageUrlId()),
                        sample.content()
                );
            }).toList();
            return new ResponseGenre1Blogs(genre1Id, blogs);
        }).toList();

        return response;
    }

}
