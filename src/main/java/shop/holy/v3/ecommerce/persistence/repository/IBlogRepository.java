package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Blog;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_BlogRow_Genre1Id;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IBlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    Optional<Blog> findFirstByIdAndDeletedAtIsNull(long id);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"profile", "genre2s"})
    Page<Blog> findAll(Specification<Blog> spec, @NonNull Pageable pageable);

    @Modifying
    @Query("UPDATE Blog b SET b.deletedAt = CURRENT_TIMESTAMP WHERE b.id = :id")
    int updateBlogDeletedAtById(long id);

    @Modifying
    @Query("UPDATE Blog b SET b.deletedAt = CURRENT_TIMESTAMP WHERE b.id in (:ids)")
    int updateBlogDeletedAtByIdIn(long[] ids);


    @Modifying
    @Query(value = """
            UPDATE blogs b SET
            title = COALESCE(:#{#blog.title}, title),
            subtitle = COALESCE(:#{#blog.subtitle}, subtitle),
            content = COALESCE(:#{#blog.content}, content),
            published_at = COALESCE(:#{#blog.publishedAt}, published_at),
                image_url_id = COALESCE(:#{#blog.imageUrlId}, image_url_id)
            WHERE id = :id AND b.profile_id = :profileId
            """, nativeQuery = true)
    void updateBlogIfNotNull(@Param("blog") Blog blog, @Param("id") long id, @Param("profileId") long profileId);

    @Query(value = """
            SELECT gid, r.id, r.title, r.subtitle, r.content,
                   r.created_at, r.published_at, r.approved_at, r.image_url_id,
                   p.id, p.full_name, p.created_at, p.image_url_id, r.g2Id
                FROM unnest(:genre1Ids) AS gid
                JOIN LATERAL (
                    SELECT DISTINCT b.*, g2.id as g2Id
                    FROM blogs b
                    JOIN blogs_genres bg ON bg.blog_id = b.id
                    JOIN genre2 g2 ON bg.genre2_id = g2.id
                    WHERE g2.genre1_id = gid
                          AND b.deleted_at IS NULL
                          AND b.approved_at IS NOT NULL
                    ORDER BY b.published_at DESC NULLS LAST
                    LIMIT :size
                ) r join profiles p on p.id= r.profile_id ON TRUE
            """, nativeQuery = true)
    List<ProQ_BlogRow_Genre1Id> findBlogsLateralNotDeleted(Long[] genre1Ids, int size);


    @Modifying
    @Query(value = """
            UPDATE Blog b set b.approvedAt = :approvedAt
            where b.id=:id
            """)
    int updateApprovedAtById(Long id, Date approvedAt);

}
