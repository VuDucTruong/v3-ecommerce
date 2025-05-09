package shop.holy.v3.ecommerce.api.dto.mail;

import lombok.Getter;
import lombok.Setter;
import org.thymeleaf.context.Context;

@Getter
@Setter
public class MailWelcome {
    String domain;
    String token;
    String email;

    public void bindContext(Context context) {
        context.setVariable("domain", domain);
        context.setVariable("token", token);
        context.setVariable("email", email);
    }
}
