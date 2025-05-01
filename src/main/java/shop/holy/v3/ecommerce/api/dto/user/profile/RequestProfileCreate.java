package shop.holy.v3.ecommerce.api.dto.user.profile;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public record RequestProfileCreate(
        @Length(max = 50, min = 1) String fullName,
        MultipartFile avatar// File input for image
) {
}
