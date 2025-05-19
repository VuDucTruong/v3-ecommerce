package shop.holy.v3.ecommerce.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static shop.holy.v3.ecommerce.shared.constant.DefaultValues.ClaimKeys;

@Data
@NoArgsConstructor
public class AuthAccount implements UserDetails {

    private long id;
    private Long profileId;
    private LocalDate enableDate;
    private LocalDate disableDate;
    private Date deletedAt;

    private String email;

    private String password;
    private Boolean isVerified;
    private RoleEnum role;

    public AuthAccount(long userId) {
        this.id = userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(role.getRoles())
                .map(role -> (GrantedAuthority) () -> role)
                .toList();
    }

    public void fromClaims(Claims claims) {
        id = claims.get(ClaimKeys.ID, Long.class);
        profileId = claims.get(ClaimKeys.PROFILE_ID, Long.class);
        role = RoleEnum.valueOf(claims.get(ClaimKeys.ROLE, String.class));
        enableDate = parseLocalDate(claims.get(ClaimKeys.ENABLE_DATE, String.class));
        disableDate = parseLocalDate(claims.get(ClaimKeys.DISABLE_DATE, String.class));
        deletedAt = parseDateTime(claims.get(ClaimKeys.DELETED_AT, String.class));
        isVerified = claims.get(ClaimKeys.IS_VERIFIED, Boolean.class);
    }

    private LocalDate parseLocalDate(String dateStr) {
        if (dateStr == null) return null;
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            throw BizErrors.INVALID_TOKEN.exception();
        }
    }
    private Date parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null) return null;
        try {
            return Date.from(LocalDateTime.parse(dateTimeStr).atZone(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            throw BizErrors.INVALID_TOKEN.exception();
        }
    }

    public boolean isAdmin() {
        return role == RoleEnum.ADMIN;
    }
    public boolean isStaff(){
        return role == RoleEnum.STAFF;
    }
    public boolean isCustomer(){
        return role == RoleEnum.CUSTOMER;
    }

    public boolean isNotSelf(long id) {
        return this.id != id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}