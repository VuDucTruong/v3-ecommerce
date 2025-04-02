package shop.holy.v3.ecommerce.api.dto.user;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public record RequestUserSearch(
        Pageable pageRequest,
        List<Long> ids,
        String citizenId,
        String fullName,
        boolean deleted,
        LocalDate createdAtFrom,
        LocalDate createdAtTo,
        String phone,
        String email
) {
}
