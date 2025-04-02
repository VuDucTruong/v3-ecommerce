package shop.holy.v3.ecommerce.api.dto.account;

import shop.holy.v3.ecommerce.api.dto.user.profile.ResponseProfile;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;

import java.time.LocalDate;
import java.util.Date;

public record ResponseUser(
        long id,
        LocalDate enableDate,
        LocalDate disableDate,
        Date createdAt,
        Date deletedAt,
        boolean isVerified,
        RoleEnum role,
        ResponseProfile profile
) {

}
