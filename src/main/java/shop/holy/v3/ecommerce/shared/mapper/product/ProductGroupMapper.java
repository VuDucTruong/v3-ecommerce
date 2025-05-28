package shop.holy.v3.ecommerce.shared.mapper.product;


import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import shop.holy.v3.ecommerce.api.dto.product.group.RequestProductGroupCreate;
import shop.holy.v3.ecommerce.api.dto.product.group.RequestProductGroupUpdate;
import shop.holy.v3.ecommerce.api.dto.product.group.ResponseProductGroup;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductGroup;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductGroupMapper {
    public abstract ProductGroup fromCreateRequestToEntity(RequestProductGroupCreate productGroup);
    public abstract ProductGroup fromUpdateRequestToEntity(RequestProductGroupUpdate productGroup);
    public abstract ResponseProductGroup fromEntityToResponse(ProductGroup productGroup);
}
