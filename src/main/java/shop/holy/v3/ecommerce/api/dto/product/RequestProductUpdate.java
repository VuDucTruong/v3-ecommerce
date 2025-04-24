package shop.holy.v3.ecommerce.api.dto.product;

import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.api.dto.product.description.RequestProductDescription;

import java.math.BigDecimal;
import java.util.List;

public record RequestProductUpdate(
        long id,
        RequestProductDescription productDescription,
        String slug,
        String name,
        Long groupId,
        MultipartFile image,
        BigDecimal price,
        BigDecimal originalPrice,
        boolean isRepresent,
//        Date availableFrom,
//        Date availableTo,
        List<Long> catIdsToDelete,
        List<Long> catIdsToAdd
) {
}
