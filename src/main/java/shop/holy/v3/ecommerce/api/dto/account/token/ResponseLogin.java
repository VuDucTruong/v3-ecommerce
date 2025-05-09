package shop.holy.v3.ecommerce.api.dto.account.token;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import shop.holy.v3.ecommerce.api.dto.account.ResponseUser;

public record ResponseLogin (
        String accessToken,
        String refreshToken,
        @JsonUnwrapped ResponseUser profile
) {
}
