package shop.holy.v3.ecommerce.shared.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles.*;

@RequiredArgsConstructor
@Getter
public enum RoleEnum {
    ADMIN(new String[]{ROLE_LEVEL_0,ROLE_STAFF, ROLE_ADMIN}),
    STAFF(new String[]{ROLE_LEVEL_0, ROLE_STAFF}),
    CUSTOMER(new String[]{ROLE_LEVEL_0, ROLE_CUSTOMER});

    public static class Roles {
        public static final String ROLE_LEVEL_0 = "ROLE_LEVEL_0";
        //        public static final String ROLE_LEVEL_1 = "ROLE_LEVEL_1";
//        public static final String ROLE_LEVEL_2 = "ROLE_LEVEL_2";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_STAFF = "ROLE_STAFF";
        public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    }


    private final String[] roles;


}
