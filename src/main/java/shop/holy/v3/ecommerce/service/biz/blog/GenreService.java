package shop.holy.v3.ecommerce.service.biz.blog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.blog.genre.RequestGenre1Upsert;
import shop.holy.v3.ecommerce.api.dto.blog.genre.ResponseGenre1;
import shop.holy.v3.ecommerce.persistence.entity.Genre1;
import shop.holy.v3.ecommerce.persistence.repository.IGenre2Repository;
import shop.holy.v3.ecommerce.persistence.repository.IGenreRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
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
    public ResponseGenre1 upsert(RequestGenre1Upsert request) {
        Genre1 genre1 = genreMapper.fromGenreUpsertToEntity(request);
        for (var l : request.genreIds())
            genre2Repository.updateGenre2ById(genre1.getId(), l);
        return genreMapper.fromEntityToResponse(genre1);
    }

    public ResponseGenre1 getByIdentifier(Long id, String name, boolean deleted) {
        Genre1 rs;
        if (id != null)
            if (deleted)
                rs = genreRepository.findById(id).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
            else
                rs = genreRepository.findFirstByIdAndDeletedAtIsNull(id).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
        else if (deleted)
            rs = genreRepository.findFirstByName(name).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
        else
            rs = genreRepository.findFirstByNameAndDeletedAtIsNull(name).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
        return genreMapper.fromEntityToResponse(rs);
    }

    public ResponseGenre1[] getAllGenres() {
        List<Genre1> genres = genreRepository.findAll();
        return genres.stream().map(genreMapper::fromEntityToResponse).toArray(ResponseGenre1[]::new);
    }


}
