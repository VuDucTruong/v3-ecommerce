package shop.holy.v3.ecommerce.shared.util;

import jakarta.annotation.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;

import java.util.function.BooleanSupplier;

@Component
public class SecurityUtil {
    public static boolean isAuthenticated(Authentication authentication) {
        return authentication != null && !authentication.isAuthenticated();
    }

    public static AuthAccount getAuthNonNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        switch (authentication) {
            case null -> throw BizErrors.AUTHORISATION_NULL.exception();
            case AnonymousAuthenticationToken _ -> throw BizErrors.AUTHORISATION_ANNONYMOUS.exception();
            case UsernamePasswordAuthenticationToken _ -> {
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

    @Nullable
    public static AuthAccount getAuthNullable() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        switch (authentication) {
            case UsernamePasswordAuthenticationToken _ -> {
                Object authAccount = authentication.getPrincipal();
                if (authAccount == null) {
                    throw BizErrors.AUTHORISATION_INVALID.exception();
                }
                return (AuthAccount) authAccount;
            }
            case null, default -> {
            }
        }
        return null;
    }

    public static long getAuthProfileId() {
        AuthAccount authAccount = getAuthNonNull();
        if (authAccount.getProfileId() == null)
            throw BizErrors.AUTHORISATION_INVALID.exception();
        return authAccount.getProfileId();
    }

    public static void validateBizResources(RoleEnum requestRole, RoleEnum resourceRole, BooleanSupplier isOwned) {
        /// IF ADMIN, I DONT CARE
        if (requestRole == RoleEnum.MAX)
            return;
        if (requestRole.gt(resourceRole))
            throw BizErrors.RESOURCE_NOT_AUTHORISED_REQUIRE_HIGHER_POWER.exception();
        if (requestRole.equals(resourceRole) && !isOwned.getAsBoolean()) {
            throw BizErrors.RESOURCE_NOT_OWNED.exception();
        }

    }

    public static void validateBizResources(RoleEnum requestRole, String resourceRole, BooleanSupplier isOwned) {
        try {
            var roleParsed = RoleEnum.valueOf(resourceRole);
            validateBizResources(requestRole, roleParsed, isOwned);
        } catch (IllegalArgumentException e) {
            throw BizErrors.AUTHORISATION_INVALID_REQUIRE_REFRESH.exception();
        }
    }

    public static boolean nullSafeIsAdmin(AuthAccount authAccount) {
        return authAccount != null && authAccount.isAdmin();
    }

    public static boolean nullSafeIsAdmin() {
        return SecurityUtil.nullSafeIsAdmin(SecurityUtil.getAuthNullable());
    }
    public static boolean guessOrCustomer(AuthAccount authAccount) {
        return authAccount == null || authAccount.isCustomer();
    }

    public static boolean guessOrCustomer() {
        return SecurityUtil.guessOrCustomer(SecurityUtil.getAuthNullable());
    }



}
