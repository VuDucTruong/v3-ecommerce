package shop.holy.v3.ecommerce.api.dto.user.profile;

import java.time.LocalDate;

public record ResponseProfile (
        long id,
        String fullName,
        LocalDate createdAt,
        String imageUrl
) {

}
