package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Comment;

import java.util.Optional;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    @Override
    @EntityGraph(attributePaths = {"author", "replies"})
    Optional<Comment> findById(Long aLong);

    @EntityGraph(attributePaths = {"author", "replies"})
    Optional<Comment> findFirstByIdEqualsAndDeletedAtIsNull(long id);


    @Modifying
    @Query("UPDATE Comment c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.id = ?1")
    int updateDeletedAtById(long id);


}
