package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.account.token.ResponseLogin;
import shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys;
import shop.holy.v3.ecommerce.service.biz.TestService;
import shop.holy.v3.ecommerce.service.smtp.SmtpService;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.CookieKeys;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.util.Arrays;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@Tag(name = "z-test")
@RequestMapping("test")
public class ControllerTest {

    private final SmtpService smtpService;
    private final TestService testService;

    @GetMapping("/biz-errors")
    public ResponseEntity<?> testBizErrors() {
        throw BizErrors.EMAIL_ALREADY_EXISTS.exception();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0) and principal.enabled")
    public ResponseEntity<AuthAccount> testSelf() {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        return ResponseEntity.ok(authAccount);
    }

    @GetMapping("/html")
    public ResponseEntity<String> html() throws MessagingException {
        MailProductKeys mailProductKeys = MailProductKeys.sample();
        String body = smtpService.makeBodyFromMPK(mailProductKeys);

        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .body(body);
    }

    @GetMapping("tokens")
    public ResponseEntity<?> getString(@RequestParam RoleEnum role, HttpServletResponse response) {
        ResponseLogin responseLogin = testService.authenticate(role.name());
        Cookie accessTokenCookie = new Cookie(CookieKeys.ACCESS_TOKENS, responseLogin.accessToken());
        Cookie refreshTokenCookie = new Cookie(CookieKeys.REFRESH_TOKENS, responseLogin.refreshToken());
        accessTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setHttpOnly(true);

        accessTokenCookie.setPath("/");
        refreshTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(60 * 60 * 24 * 7);
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 7);
        var cookies = new Cookie[]{accessTokenCookie, refreshTokenCookie};
        for (Cookie cookie : cookies) {
            response.addCookie(cookie);
        }
        return ResponseEntity.ok(responseLogin);
    }

    @GetMapping("abc")
    public ResponseEntity<String> getString() {
        return ResponseEntity.ok("asdlfjaslkdfjakj dlkajflkjalsdjf alksdjfklasd");
    }

    @GetMapping("/hello")
    public String test() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/test/abc", String.class);
        return response.getBody();
    }


}
