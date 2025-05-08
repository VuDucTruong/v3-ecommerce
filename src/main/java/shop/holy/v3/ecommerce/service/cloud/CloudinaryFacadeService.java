package shop.holy.v3.ecommerce.service.cloud;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.property.CloudinaryProperties;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryFacadeService {

    private final CloudinaryProperties cloudinaryProperties;
    private final CloudinaryService cloudinaryService;

    public String uploadAccountBlob(MultipartFile file) {
        try {
            return cloudinaryService.uploadFile(
                    file, cloudinaryProperties.getAccountDir(), UUID.randomUUID().toString());
        } catch (IOException e) {
            return null;
        }
    }

    public String uploadProductBlob(MultipartFile file) {
        try {
            return cloudinaryService.uploadFile(file, cloudinaryProperties.getProductDir(),
                    UUID.randomUUID().toString());
        } catch (IOException e) {
            return null;

        }
    }

    public String uploadCategoryBlob(MultipartFile file) {
        try {
            return cloudinaryService.uploadFile(file, cloudinaryProperties.getCategoryDir(), UUID.randomUUID().toString());
        } catch (IOException e) {
            return null;
        }
    }

    public String uploadBlogBlob(MultipartFile file) {
        try {
            return cloudinaryService.uploadFile(file, cloudinaryProperties.getBlogDir(), UUID.randomUUID().toString());
        } catch (IOException e) {
            return null;

        }
    }

    public String uploadCouponBlob(MultipartFile file) {
        try {
            return cloudinaryService.uploadFile(file, cloudinaryProperties.getCouponDir(), UUID.randomUUID().toString());
        } catch (IOException e) {
            return null;

        }
    }

}
