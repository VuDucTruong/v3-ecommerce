package shop.holy.v3.ecommerce.shared.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import shop.holy.v3.ecommerce.api.dto.RequestPageable;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryService;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class IBaseMapper {

    @Autowired
    protected CloudinaryService cloudinaryService;

    @Named("genUrl") // Ensure MapStruct picks up this method
    public String genUrl(String publicId) {
        return (publicId != null) ? cloudinaryService.makeUrl(publicId) : null;
    }

    public Pageable fromRequestPageableToPageable(RequestPageable requestPageable) {
        if (requestPageable == null) {
            return PageRequest.of(
                    0,
                    10,
                    Sort.by(Sort.Direction.ASC, "id")
            );
        }
        Sort.Direction direction = Sort.Direction.fromOptionalString(
                        requestPageable.sortDirection())
                .orElse(Sort.Direction.ASC);
        return PageRequest.of(
                requestPageable.page(),
                requestPageable.size(),
                Sort.by(direction,
                        Optional.of(requestPageable.sortBy())
                                .orElse("id")
                )
        );
    }
}

