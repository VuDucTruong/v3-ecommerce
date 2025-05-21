package shop.holy.v3.ecommerce.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.holy.v3.ecommerce.persistence.entity.Genre2;

@Repository
public interface IGenre2Repository extends JpaRepository<Genre2,Long> {

    @Modifying
    @Query(value = """
            delete from Genre2 g2 where
            g2.genre1Id = :id
            """)
    void deleteAllByGenre1Id(long id);

    @Query("update Genre2 g set g.genre1Id = :#{#g1Id} where g.id = :#{#id}")
    @Modifying
    void updateGenre2ById(long id, long g1Id);

    @Query(value = "INSERT INTO public.blogs_genres (blog_id, genre2_id) VALUES (?1,?2)", nativeQuery = true)
    void insertBlogsGenres(long blogId, long genre1Id);


}
