package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Comment;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByIdInAndDeletedAtIsNull(Collection<Long> id);

    List<Comment> findAllByIdIn(Collection<Long> id);

    Optional<Comment> findFirstByIdEqualsAndDeletedAtIsNull(long id);


    @Modifying
    @Query("UPDATE Comment c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.id = ?1")
    int updateDeletedAtById(long id);


}
