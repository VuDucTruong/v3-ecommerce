package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Blog;

import java.util.Optional;

@Repository
public interface IBlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    Optional<Blog> findFirstByIdAndDeletedAtIsNull(long id);

    @Query("UPDATE Blog b SET b.deletedAt = CURRENT_TIMESTAMP WHERE b.id = :id")
    int updateBlogDeletedAt(long id);

}
