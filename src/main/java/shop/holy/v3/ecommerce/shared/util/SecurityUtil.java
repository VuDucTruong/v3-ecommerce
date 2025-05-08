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
    public static boolean isAuthenticated(Authentication authentication) {
        return authentication != null && !authentication.isAuthenticated();
    }

    public static AuthAccount getAuthNonNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        switch (authentication) {
            case null -> throw BizErrors.AUTHORISATION_NULL.exception();
            case AnonymousAuthenticationToken ignored -> throw BizErrors.AUTHORISATION_ANNONYMOUS.exception();
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
        throw BizErrors.AUTHORISATION_INVALID.exception();
    }

    public static long getAuthProfileId() {
        AuthAccount authAccount = getAuthNonNull();
        if (authAccount.getProfileId() == null)
            throw BizErrors.AUTHORISATION_INVALID.exception();
        return authAccount.getProfileId();
    }

    public static AuthAccount getAuthMatchingRoleNullSafe(RoleEnum roleEnum) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        if (authAccount.getRole() == null) {
            throw BizErrors.AUTHORISATION_INVALID.exception();
        } else if (!authAccount.getRole().equals(roleEnum)) {
            throw BizErrors.AUTHORISATION_INVALID.exception();
        }
        return authAccount;
    }

}
