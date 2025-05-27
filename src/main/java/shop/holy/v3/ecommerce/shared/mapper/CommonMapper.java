package shop.holy.v3.ecommerce.shared.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryService;
import shop.holy.v3.ecommerce.shared.constant.MapFuncs;
import shop.holy.v3.ecommerce.shared.property.CloudinaryProperties;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class CommonMapper {

    @Autowired
    protected CloudinaryService cloudinaryService;

    @Autowired
    protected CloudinaryProperties cloudinaryProperties;

    @Named("genUrl") // Ensure MapStruct picks up this method
    public String genUrl(String publicId) {
        return (publicId != null) ? cloudinaryService.makeUrl(publicId) : null;
    }


    @Named(MapFuncs.EXTRACT_ACCOUNT_PUBLIC_ID)
    public String extractAccountPublicId(String url) {
        return (url != null) ? cloudinaryService.extractPublicId(url, cloudinaryProperties.getAccountDir()) : null;
    }

    @Named(MapFuncs.EXTRACT_PRODUCT_PUBLIC_ID)
    public String extractProductPublicId(String url) {
        return (url != null) ? cloudinaryService.extractPublicId(url, cloudinaryProperties.getProductDir()) : null;
    }

    @Named(MapFuncs.EXTRACT_CATEGORY_PUBLIC_ID)
    public String extractCategoryPublicId(String url) {
        return (url != null) ? cloudinaryService.extractPublicId(url, cloudinaryProperties.getCategoryDir()) : null;
    }

    @Named(MapFuncs.EXTRACT_BLOG_PUBLIC_ID)
    public String extractBlogPublicId(String url) {
        return (url != null) ? cloudinaryService.extractPublicId(url, cloudinaryProperties.getBlogDir()) : null;
    }

}

