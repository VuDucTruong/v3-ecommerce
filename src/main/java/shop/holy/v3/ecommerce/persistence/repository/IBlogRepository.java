package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Blog;

import java.util.Optional;

@Repository
public interface IBlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    Optional<Blog> findFirstByIdAndDeletedAtIsNull(long id);

    @Override
    @EntityGraph(attributePaths = {"profile",  "genre2s"})
    Page<Blog> findAll(Specification<Blog> spec, Pageable pageable);

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

}
