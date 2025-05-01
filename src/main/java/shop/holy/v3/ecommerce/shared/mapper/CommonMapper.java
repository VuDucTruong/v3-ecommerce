package shop.holy.v3.ecommerce.shared.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryService;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class CommonMapper {

    @Autowired
    protected CloudinaryService cloudinaryService;

    @Named("genUrl") // Ensure MapStruct picks up this method
    public String genUrl(String publicId) {
        return (publicId != null) ? cloudinaryService.makeUrl(publicId) : null;
    }
}

