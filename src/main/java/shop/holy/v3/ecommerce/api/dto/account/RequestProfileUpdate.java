package shop.holy.v3.ecommerce.api.dto.account;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public record RequestProfileUpdate(
        Long id,
        String fullName,
//        String imageUrl,
        MultipartFile image
) {

}
