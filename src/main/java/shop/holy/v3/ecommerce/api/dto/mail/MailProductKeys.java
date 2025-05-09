package shop.holy.v3.ecommerce.api.dto.mail;

import lombok.Getter;
import lombok.Setter;
import org.thymeleaf.context.Context;

import java.util.Date;

@Getter
@Setter
public class MailProductKeys {
    String fullName;
//    String bannerUrl;
    ProductKey[] keys;

    @Getter
    @Setter
    public static class ProductKey {
        String productKey;
        Date startDate;
        Date expiryDate;
        String duration;
    }

    public void bindContext(Context context) {
        context.setVariable("fullName", fullName);
//        context.setVariable("bannerUrl", bannerUrl);
        context.setVariable("keys", keys);
    }

}
