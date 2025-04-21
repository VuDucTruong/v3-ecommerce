package shop.holy.v3.ecommerce.api.dto.product;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record RequestProductUpdate(
        long id,
        RequestProductDescription productDescription,
        String slug,
        String name,
        Long parentId,
        MultipartFile image,
        String description,
        BigDecimal price,
        BigDecimal originalPrice,
//        Date availableFrom,
//        Date availableTo,
        List<Long> catIdsToDelete,
        List<Long> catIdsToAdd
) {
}
