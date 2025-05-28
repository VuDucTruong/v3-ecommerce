package shop.holy.v3.ecommerce.shared.mapper.product;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.util.CollectionUtils;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductTag;
import shop.holy.v3.ecommerce.shared.constant.MapFuncs;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductTagMapper {


    @Named(MapFuncs.FROM_TAG_ENTITY_TO_STRING_ARRAY)
    default String[] fromTagEntitiesToStringTags(Set<ProductTag> tags) {
        if (CollectionUtils.isEmpty(tags))
            return new String[0];
        return tags.stream().map(ProductTag::getName).toArray(String[]::new);
    }


    @Named(MapFuncs.FROM_LIST_STRING_TO_TAG_ENTITIES)
    default Set<ProductTag> fromStringTagsToProductTagsEntity(long id, List<String> tags) {
        return tags.stream().map(str -> {
            var tag = new ProductTag();
            tag.setProductId(id);
            tag.setName(str);
            return tag;
        }).collect(Collectors.toSet());
    }


}
