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
import java.util.function.Predicate;

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
            case null -> {
            }
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
        if (requestRole.equals(resourceRole) && isOwned.getAsBoolean()) {
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

    public static AuthAccount getAuthMatchingRoleNullSafe(RoleEnum roleEnum) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        if (authAccount.getRole() == null) {
            throw BizErrors.AUTHORISATION_INVALID.exception();
        } else if (!authAccount.getRole().equals(roleEnum)) {
            throw BizErrors.AUTHORISATION_INVALID.exception();
        }
        return authAccount;
    }

    public Long getAuthAccountId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AuthAccount authAccount = (AuthAccount) authentication.getPrincipal();
            return authAccount.getId();
        } catch (Exception e) {
            return null;
        }
    }
}
