package shop.holy.v3.ecommerce.api.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.account.RequestAccountRegistration;
import shop.holy.v3.ecommerce.api.dto.account.RequestOTP;
import shop.holy.v3.ecommerce.api.dto.account.RequestPasswordUpdate;
import shop.holy.v3.ecommerce.api.dto.account.token.RequestLogin;
import shop.holy.v3.ecommerce.api.dto.account.token.ResponseLogin;
import shop.holy.v3.ecommerce.service.biz.AccountService;
import shop.holy.v3.ecommerce.service.security.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("accounts")
public class ControllerAccount {

    private final AccountService accountService;
    private final AuthService authService;

    @PostMapping(value = "/otp")
    public ResponseEntity<?> verifyOtp(@RequestBody RequestOTP request) throws MessagingException {
        accountService.saveOtp(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/password")
    public ResponseEntity<?> changePassword(@RequestBody RequestPasswordUpdate request) {
        accountService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/registration")
    public ResponseEntity<?> register(@RequestBody RequestAccountRegistration request) {
        accountService.registerAccount(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("")
    public ResponseEntity<?> getTokens(@RequestBody RequestLogin loginRequest, HttpServletResponse response) {
        ResponseLogin loginResponse = authService.authenticateAccount(loginRequest);
        Cookie[] cookies = authService.makeCookies(loginResponse);
        for (Cookie cookie : cookies) {
            response.addCookie(cookie);
        }
        return ResponseEntity.ok(loginResponse);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteTokens(HttpServletResponse response) {
        Cookie[] cookies = authService.removeAuthCookies();
        for (Cookie cookie : cookies) {
            response.addCookie(cookie);
        }
        return ResponseEntity.ok().build();
    }

}
