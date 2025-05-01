package shop.holy.v3.ecommerce.shared.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public abstract class PaymentMapper
{

}
