package shop.holy.v3.ecommerce.api.dto;

import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;

import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
public class AuthAccount implements UserDetails {

    private long id;
    private Integer code;
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
        return Collections.singleton((GrantedAuthority) () -> role.name());
    }

    public void fromClaims(Claims claims) {
        id = claims.get("userId", Long.class);
        code = claims.get("search", Integer.class);
        role = RoleEnum.valueOf(claims.get("role", String.class));
        enableDate = claims.get("enableDate", LocalDate.class);
        disableDate = claims.get("disableDate", LocalDate.class);
        deletedAt = claims.get("deletedAt", Date.class);
        isVerified = claims.get("isVerified", Boolean.class);
    }

    public boolean isAdmin() {
        return role == RoleEnum.ROLE_ADMIN;
    }

    public boolean isCustomer() {
        return role == RoleEnum.ROLE_CUSTOMER;
    }

    public boolean isNotSelf(long id) {
        return this.id != id;
    }

    public boolean isNotSelfNotAdmin(long id) {
        return this.id != id && role != RoleEnum.ROLE_ADMIN;
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
