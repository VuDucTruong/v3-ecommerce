package shop.holy.v3.ecommerce.shared.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles.*;

@RequiredArgsConstructor
@Getter
public enum RoleEnum {

    ADMIN(new String[]{ROLE_LEVEL_0, ROLE_STAFF, ROLE_ADMIN}, 10),
    STAFF(new String[]{ROLE_LEVEL_0, ROLE_STAFF}, 5),
    CUSTOMER(new String[]{ROLE_LEVEL_0, ROLE_CUSTOMER}, 1);

    public static class Roles {
        public static final String ROLE_LEVEL_0 = "ROLE_LEVEL_0";
        //        public static final String ROLE_LEVEL_1 = "ROLE_LEVEL_1";
//        public static final String ROLE_LEVEL_2 = "ROLE_LEVEL_2";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_STAFF = "ROLE_STAFF";
        public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    }
    public static RoleEnum MAX = ADMIN;

    private final String[] roles;
    private final int power;

    public boolean lt(RoleEnum role) {
        return this.power < role.power;
    }

    public boolean gt(RoleEnum role) {
        return this.power > role.power;
    }



}
