package shop.holy.v3.ecommerce.shared.mapper;

import org.mapstruct.*;
import shop.holy.v3.ecommerce.api.dto.blog.genre.RequestGenreUpsert;
import shop.holy.v3.ecommerce.api.dto.blog.genre.ResponseGenre;
import shop.holy.v3.ecommerce.persistence.entity.Genre1;
import shop.holy.v3.ecommerce.persistence.entity.Genre2;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class GenreMapper {

    public abstract Genre1 fromGenreUpsertToEntity(RequestGenreUpsert request);

    @Mapping(source = "genre2s", target = "genres", qualifiedByName = "fromGenre2sToEntity")
    public abstract ResponseGenre fromEntityToResposne(Genre1 genre);

    public abstract ResponseGenre fromRequestToResponse(RequestGenreUpsert request);

    @Named("fromGenre2sToEntity")
    public String[] fromGenre2sToEntity(Set<Genre2> genre2s) {
        if (genre2s == null || genre2s.isEmpty())
            return new String[0];
        return genre2s.stream()
                .map(Genre2::getName)
                .toArray(String[]::new);
    }


    public List<Genre2> from_StringArr_To_Genre2s(List<String> genres, long genre1Id) {
        return genres.stream().map(
                str -> {
                    Genre2 genre2 = new Genre2();
                    genre2.setName(str);
                    genre2.setGenre1Id(genre1Id);
                    return genre2;
                }
        ).toList();
    }

}
