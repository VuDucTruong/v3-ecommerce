package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.util.Arrays;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@Tag(name = "z-test")
public class ControllerTest {


    @GetMapping("/biz-errors")
    public ResponseEntity<?> testBizErrors() {
        throw BizErrors.EMAIL_ALREADY_EXISTS.exception();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<AuthAccount> testSelf() {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        return ResponseEntity.ok(authAccount);
    }


}
