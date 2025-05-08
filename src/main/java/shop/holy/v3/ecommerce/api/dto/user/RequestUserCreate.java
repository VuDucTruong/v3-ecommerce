package shop.holy.v3.ecommerce.api.dto.user;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import shop.holy.v3.ecommerce.api.dto.user.profile.RequestProfileCreate;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;

import java.util.Date;

public record RequestUserCreate(

        @Email
        String email,
        @Length(min = 6, max = 40)
        String password,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        Date enableDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        Date disableDate,
        Boolean isVerified,
        RoleEnum role,
        RequestProfileCreate profile
) {
}
