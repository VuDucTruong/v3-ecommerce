package shop.holy.v3.ecommerce.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class ControllerTest {


    @GetMapping("/biz-errors")
    public ResponseEntity<?> testBizErrors() {
        throw BizErrors.COUPON_ALREADY_USED.exception();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<AuthAccount> testSelf() {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        return ResponseEntity.ok(authAccount);
    }

}
