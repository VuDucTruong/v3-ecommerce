package shop.holy.v3.ecommerce.shared.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;
import shop.holy.v3.ecommerce.shared.exception.UnAuthorisedException;

public class SecurityUtil {

    public static AuthAccount getAuthNonNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        switch (authentication) {
            case null -> throw BizErrors.AUTHORISATION_NULL.exception();
            case AnonymousAuthenticationToken ignored ->
                    throw BizErrors.AUTHORISATION_ANNONYMOUS.exception();
            case UsernamePasswordAuthenticationToken ignored -> {
                Object authAccount = authentication.getPrincipal();
                if (authAccount == null) {
                    throw BizErrors.AUTHORISATION_INVALID.exception();
                }
                return (AuthAccount) authAccount;
            }
            default -> {
            }
        }
        throw new UnAuthorisedException("Unauthorised Unknown Error ");
    }

    public static AuthAccount getAuthMatchingRoleNullSafe(RoleEnum roleEnum) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        if (authAccount.getRole() == null) {
            throw new UnAuthorisedException("Unauthorised Null Role");
        } else if (!authAccount.getRole().equals(roleEnum)) {
            throw new UnAuthorisedException("Unauthorised Role");
        }
        return authAccount;
    }

}
