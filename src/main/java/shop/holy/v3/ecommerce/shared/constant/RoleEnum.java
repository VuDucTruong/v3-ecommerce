package shop.holy.v3.ecommerce.shared.constant;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_STAFF("ROLE_STAFF"),
    ROLE_CUSTOMER("ROLE_CUSTOMER");

    private final String role;

    RoleEnum(String role) {
        this.role = role;
    }
}
