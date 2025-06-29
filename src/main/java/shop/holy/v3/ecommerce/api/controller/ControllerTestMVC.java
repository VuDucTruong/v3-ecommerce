package shop.holy.v3.ecommerce.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys;
import shop.holy.v3.ecommerce.service.biz.notification.MailCommand;
import shop.holy.v3.ecommerce.service.smtp.SmtpService;

import java.util.Date;

@Controller
@RequiredArgsConstructor
public class ControllerTestMVC {
    private final SmtpService smtpService;


    @RequestMapping("/welcome")
    public String index(Model model) {
        model.addAttribute("bannerUrl", "https://res.cloudinary.com/dm45tt6nt/image/upload/v1751191772/popqmvsqxxb2sqzjskdz.png");
        model.addAttribute("domain", "www.google.com");
        model.addAttribute("token", "password");
        return "welcome";
    }

    @RequestMapping("/product_key")
    public String product_key(Model model) {
        MailProductKeys sample = MailProductKeys.sample();
        String body = smtpService.makeBodyFromMPK(sample);
        return body;
    }


}
