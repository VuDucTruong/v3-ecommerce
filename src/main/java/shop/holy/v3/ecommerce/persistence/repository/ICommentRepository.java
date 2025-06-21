package shop.holy.v3.ecommerce.persistence.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Comment;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_AccountId_Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    @Nonnull
    @Override
    @EntityGraph(attributePaths = {"author", "product", "replies","author.account"})
    Page<Comment> findAll(Specification<Comment> spec, @Nonnull Pageable pageable);

    @Nonnull
    @Override
    @EntityGraph(attributePaths = {"author", "replies", "product", "author.account"})
    Optional<Comment> findById(@Nonnull Long aLong);

    @EntityGraph(attributePaths = {"author", "replies", "product","author.account"})
    Optional<Comment> findByIdAndDeletedAtIsNull(Long aLong);

    @EntityGraph(attributePaths = {"author", "replies","author.account"})
    Page<Comment> findAllByProductIdAndParentCommentIdIsNull(long productId, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "replies","author.account"})
    Page<Comment> findAllByAuthorIdAndParentCommentIdIsNullAndDeletedAtIsNull(long authorId, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "replies","author.account"})
    Page<Comment> findAllByAuthorIdAndParentCommentIdIsNull(long authorId, Pageable pageable);


    @Modifying
    @Query(value = """
            UPDATE comments c SET deleted_at = CURRENT_TIMESTAMP 
            from public.profiles p join accounts a on p.account_id = a.id 
                        WHERE c.id = ?1 and c.id = p.id returning a.id, a.role
            """, nativeQuery = true)
    Optional<ProQ_AccountId_Role> updateDeletedAtById(long id);


    @Modifying
    @Query("UPDATE Comment c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.id IN (:ids)")
    List<ProQ_AccountId_Role> updateDeletedAtByIdIn(long[] ids);


    @Query(value = "update comments c set content = :content where c.id = :id returning *", nativeQuery = true)
    @Modifying
    Comment updateContentById(String content, long id);
}
