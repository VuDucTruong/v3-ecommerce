package shop.holy.v3.ecommerce.service.smtp;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys;
import shop.holy.v3.ecommerce.api.dto.mail.MailWelcome;

@RequiredArgsConstructor
@Service
public class SmtpService {

    @Value("${phong.mail.to}")
    private String fallbackTo;
    @Value("${phong.mail.enable-fallback}")
    private boolean enableFallback;
    @Value("${phong.mail.banner-url}")
    private String bannerUrl;

    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final String DEFAULT_HTML_MESSAGE = """
            <html>
            <body>
                <h1>Your OTP is: %s</h1>
                <p>Please use this OTP to complete your transaction.</p>
            </body>
            </html>
            """;


    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        if (enableFallback)
            helper.setTo(this.fallbackTo);
        else
            helper.setTo(to);
        helper.setText(body, true);
        // Enable HTML

        mailSender.send(message);
    }

    public void sendOTPEmail(String to, String otp) throws MessagingException {
        String body = String.format(DEFAULT_HTML_MESSAGE, otp);
        sendEmail(to, "Holy Shop: Your OTP Code is ...", body);
    }

    public void sendMailProductKeys(String to, @NonNull String subject, MailProductKeys prod_keys) throws MessagingException {
        Context context = new Context();
        context.setVariable("bannerUrl", bannerUrl);
        prod_keys.bindContext(context);
        String body = templateEngine.process("product_key", context);
        sendEmail(to, subject, body);
    }

    public void sendMailWelcome(String to, String subject, MailWelcome welcome) throws MessagingException {
        Context context = new Context();
        context.setVariable("bannerUrl", bannerUrl);
        welcome.bindContext(context);
        String body = templateEngine.process("welcome", context);
        sendEmail(to, subject, body);
    }

}