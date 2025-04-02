package shop.holy.v3.ecommerce.shared.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.authentication")
public class JwtProperties {

    private String accessTokenSecret;
    private String refreshTokenSecret;
    private long accessTokenExpirationMinutes;
    private long refreshTokenExpirationMinutes;
    private String issuer;
    private String audience;

}
