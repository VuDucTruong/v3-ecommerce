package shop.holy.v3.ecommerce.api.dto.user;

import shop.holy.v3.ecommerce.api.dto.RequestPageable;

import java.time.LocalDate;
import java.util.List;

public record RequestUserSearch(
        RequestPageable pageRequest,
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
