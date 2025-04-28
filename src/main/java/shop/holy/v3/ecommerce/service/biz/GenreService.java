package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.blog.genre.RequestGenreUpsert;
import shop.holy.v3.ecommerce.api.dto.blog.genre.ResponseGenre;
import shop.holy.v3.ecommerce.persistence.entity.Genre1;
import shop.holy.v3.ecommerce.persistence.repository.IGenre2Repository;
import shop.holy.v3.ecommerce.persistence.repository.IGenreRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.DefaultValues;
import shop.holy.v3.ecommerce.shared.mapper.GenreMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final IGenreRepository genreRepository;
    private final IGenre2Repository genre2Repository;
    private final GenreMapper genreMapper;

    @Transactional
    public void deleteGenre(long id) {
        int changes = genreRepository.updateDeletedAtById(id);
        if (changes == 0)
            throw BizErrors.RESOURCE_NOT_FOUND.exception();
    }

    @Transactional
    public ResponseGenre upsert(RequestGenreUpsert request) {
        Genre1 genre1 = genreMapper.fromGenreUpsertToEntity(request);
        Genre1 rs = genreRepository.save(genre1);
        genre2Repository.deleteAllByGenre1Id(genre1.getId());
        var genre2s = genreMapper.from_StringArr_To_Genre2s(request.genres(), rs.getId());
        genre2Repository.saveAll(genre2s);

        return genreMapper.fromRequestToResponse(request);
    }

    public ResponseGenre getByIdentifier(long id, String name, boolean deleted) {
        Genre1 rs;
        if (id != DefaultValues.ID)
            if (deleted)
                rs = genreRepository.findById(id).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
            else
                rs = genreRepository.findFirstByIdAndDeletedAtIsNull(id).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
        else if (deleted)
            rs = genreRepository.findFirstByName(name).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
        else
            rs = genreRepository.findFirstByNameAndDeletedAtIsNull(name).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
        return genreMapper.fromEntityToResposne(rs);
    }

    public ResponseGenre[] getAllGenres() {
        List<Genre1> genres = genreRepository.findAll();
        return genres.stream().map(genreMapper::fromEntityToResposne).toArray(ResponseGenre[]::new);
    }

}
