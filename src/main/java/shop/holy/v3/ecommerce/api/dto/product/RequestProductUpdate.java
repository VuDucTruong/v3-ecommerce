package shop.holy.v3.ecommerce.api.dto.product;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record RequestProductUpdate(
        long id,
        Long tosId,
        String slug,
        String name,
        MultipartFile image,
        String description,
        BigDecimal price,
        Double discountPercent,
        Date availableFrom,
        Date availableTo,
        List<String> categoryIds
) {
}
