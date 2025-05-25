package shop.holy.v3.ecommerce.persistence.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Comment;

import java.util.Optional;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    @Nonnull
    @Override
    @EntityGraph(attributePaths = {"author", "replies", "product"})
    Page<Comment> findAll(Specification<Comment> spec, @Nonnull Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"author", "replies", "product"})
    Optional<Comment> findById(Long aLong);

    @EntityGraph(attributePaths = {"author", "replies", "product"})
    Optional<Comment> findByIdAndDeletedAtIsNull(Long aLong);

    @EntityGraph(attributePaths = {"author", "replies"})
    Page<Comment> findAllByProductIdAndParentCommentIdIsNull(long productId, Pageable pageable);


    @Modifying
    @Query("UPDATE Comment c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.id = ?1")
    int updateDeletedAtById(long id);


    @Modifying
    @Query("UPDATE Comment c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.id IN (:ids)")
    int updateDeletedAtByIdIn(long[] ids);


    @Query(value = "update comments c set content = :content where c.id = :id returning *", nativeQuery = true)
    @Modifying
    Comment updateContentById(String content, long id);
}
