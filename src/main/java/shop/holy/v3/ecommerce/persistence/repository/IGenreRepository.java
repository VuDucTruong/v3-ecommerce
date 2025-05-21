package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Genre1;

import java.util.List;
import java.util.Optional;

@Repository
public interface IGenreRepository extends JpaRepository<Genre1, Long> {

    @Override
    @EntityGraph(attributePaths = {"genre2s"})
    List<Genre1> findAll();

    @Modifying
    @Query("update Genre1 g set g.deletedAt = CURRENT_TIMESTAMP where g.id = :id")
    int updateDeletedAtById(long id);

    Optional<Genre1> findFirstByIdAndDeletedAtIsNull(long id);

    Optional<Genre1> findFirstByNameAndDeletedAtIsNull(String name);

    Optional<Genre1> findFirstByName(String name);

}
