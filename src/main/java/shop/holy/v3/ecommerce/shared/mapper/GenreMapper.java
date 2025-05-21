package shop.holy.v3.ecommerce.shared.mapper;

import org.mapstruct.*;
import shop.holy.v3.ecommerce.api.dto.blog.genre.RequestGenre1Upsert;
import shop.holy.v3.ecommerce.api.dto.blog.genre.ResponseGenre1;
import shop.holy.v3.ecommerce.persistence.entity.Genre1;
import shop.holy.v3.ecommerce.persistence.entity.Genre2;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class GenreMapper {

    public abstract Genre1 fromGenreUpsertToEntity(RequestGenre1Upsert request);

    @Mapping(source = "genre2s", target = "genres")
    public abstract ResponseGenre1 fromEntityToResponse(Genre1 genre);

//    public abstract Genre2 fromRequestGenre2ToEntity(RequestGenre1Upsert.RequestGenre2Upsert genre2);


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
