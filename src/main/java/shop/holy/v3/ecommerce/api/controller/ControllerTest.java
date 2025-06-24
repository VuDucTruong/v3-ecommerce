package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys;
import shop.holy.v3.ecommerce.service.biz.notification.NotificationQuery;
import shop.holy.v3.ecommerce.service.smtp.SmtpService;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

@RestController
@RequiredArgsConstructor
@Tag(name = "z-test")
@RequestMapping("test")
@Slf4j
public class ControllerTest {

    private final SmtpService smtpService;
    private final NotificationQuery notificationQuery;

    @GetMapping("testnoti")
    public ResponseEntity<?> getnoti() {
        var rs = notificationQuery.getNotificationProdKeys();
        return ResponseEntity.ok().build();
    }


    @GetMapping("/biz-errors")
    public ResponseEntity<?> testBizErrors(@RequestParam("e") BizErrors error) {
        throw error.exception();
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


    @GetMapping("hello")
    public ResponseEntity<String> getString() {
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/test-trace-id-2-step")
    public String test() {
        log.info("---------Hello method started---------");
        log.error("---------Hello method started, id missing!---------");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/test/hello", String.class);
        return response.getBody();
    }


}
