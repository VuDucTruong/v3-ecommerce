package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Comment;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    List<Comment> findAllByIdInAndDeletedAtIsNull(Collection<Long> id);

    List<Comment> findAllByIdIn(Collection<Long> id);

    @Override
    @EntityGraph(attributePaths = {"author", "replies"})
    Optional<Comment> findById(Long aLong);

    @EntityGraph(attributePaths = {"author", "replies"})
    Optional<Comment> findFirstByIdEqualsAndDeletedAtIsNull(long id);


    Page<Comment> findAllByProductIdEqualsAndDeletedAtIsNull(long productId, org.springframework.data.domain.Pageable pageable);

    @Modifying
    @Query("UPDATE Comment c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.id = ?1")
    int updateDeletedAtById(long id);


}
