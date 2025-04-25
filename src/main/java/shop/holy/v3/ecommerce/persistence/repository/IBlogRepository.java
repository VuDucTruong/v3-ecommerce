package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Blog;

import java.util.Optional;

@Repository
public interface IBlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    Optional<Blog> findFirstByIdAndDeletedAtIsNull(long id);

    @Modifying
    @Query("UPDATE Blog b SET b.deletedAt = CURRENT_TIMESTAMP WHERE b.id = :id")
    int updateBlogDeletedAtById(long id);



    @Modifying
    @Query(value = """
    UPDATE blogs b SET
    title = COALESCE(:#{#blog.title}, title),
    subtitle = COALESCE(:#{#blog.subtitle}, subtitle),
    content = COALESCE(:#{#blog.content}, content),
    published_at = COALESCE(:#{#blog.publishedAt}, published_at)
    WHERE id = :id AND b.profile_id = :profileId
    """, nativeQuery = true)
    int updateBlogIfNotNull(@Param("blog") Blog blog, @Param("id") long id, @Param("profileId") long profileId);

}
