package shop.holy.v3.ecommerce.api.dto.account.token;

import shop.holy.v3.ecommerce.api.dto.account.ResponseUser;

public record ResponseLogin (
        String accessToken,
        String refreshToken,
        ResponseUser user
) {
}
