package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.account.RequestAccountRegistration;
import shop.holy.v3.ecommerce.api.dto.account.RequestMailVerification;
import shop.holy.v3.ecommerce.api.dto.account.RequestOTP;
import shop.holy.v3.ecommerce.api.dto.account.RequestPasswordUpdate;
import shop.holy.v3.ecommerce.api.dto.account.token.RequestLogin;
import shop.holy.v3.ecommerce.api.dto.account.token.ResponseLogin;
import shop.holy.v3.ecommerce.service.biz.user.AccountCommand;
import shop.holy.v3.ecommerce.service.security.AuthService;

@Tag(name = "Accounts", description = "-> to serve functional ops, auths, cookies,...")
@RestController
@RequiredArgsConstructor
@RequestMapping("accounts")
public class ControllerAccount {

    private final AccountCommand accountService;
    private final AuthService authService;

    @PostMapping(value = "otp")
    @Operation(description = "send OTP to user's email -> route to change password page with current Email context")
    public ResponseEntity<Integer> verifyOtp(@RequestBody RequestOTP request) throws MessagingException {
        accountService.saveOtp(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "password")
    @Operation(description = "change password -> Remove cookies -> must route user to login !!")
    public ResponseEntity<Integer> changePassword(@RequestBody RequestPasswordUpdate request, HttpServletRequest servletRequest, HttpServletResponse response) {
        var cookies = accountService.changePassword(request,servletRequest);
        for (ResponseCookie cookie : cookies) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "verification")
    @Operation(description = "setVerified -> Remove Cookies -> must route to login !!")
    public ResponseEntity<Integer> setVerification(@RequestBody RequestMailVerification request, HttpServletRequest servletRequest, HttpServletResponse response) {
        var cookies = accountService.verifyEmail(request, servletRequest);
        for (ResponseCookie cookie : cookies) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping("register")
    @Operation(description = "register a new user -> must route user to login !!")
    public ResponseEntity<Integer> register(@RequestBody RequestAccountRegistration request) {
        accountService.registerAccount(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("login")
    @Operation(description = "login a user -> route to home with profile as client's global context")
    public ResponseEntity<ResponseLogin> login(@RequestBody RequestLogin loginRequest, HttpServletRequest request, HttpServletResponse response) {
        ResponseLogin loginResponse = authService.authenticateAccount(loginRequest);
        ResponseCookie[] cookies = authService.makeCookies(loginResponse,request);
        for (ResponseCookie cookie : cookies) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return ResponseEntity.ok(loginResponse);
    }

    @DeleteMapping("logout")
    @Operation(description = "logout a user -> remove cookies, must route to login page")
    public ResponseEntity<Integer> logout(HttpServletResponse response, HttpServletRequest request) {
        ResponseCookie[] cookies = authService.removeAuthCookies(request);
        for (ResponseCookie cookie : cookies) {
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        return ResponseEntity.ok().build();
    }

}
