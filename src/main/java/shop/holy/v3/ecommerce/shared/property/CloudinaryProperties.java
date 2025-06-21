package shop.holy.v3.ecommerce.shared.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "phong.cloudinary")
public class CloudinaryProperties {
    private String cloudName;
    private String apiKey;
    private String apiSecret;

    private String accountDir;
    private String productDir;
    private String categoryDir;

    private String blogDir;
    private String couponDir;
}
