package shop.holy.v3.ecommerce.shared.property;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "payment.vnp")
@ToString
public class Vnp_Pay_Properties {
    private String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private String vnp_ReturnUrl;
    private String vnp_TmnCode;
    private String secretKey;
    private String vnp_Version;
    private String vnp_Command;
    private String orderType;

    public Map<String, String> buildParamsMap(String transRef, String orderInfo, String returnUrl) {
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnp_Version);
        vnpParamsMap.put("vnp_Command", this.vnp_Command);
        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_TxnRef", transRef);
        if (StringUtils.hasText(orderInfo)) {
            vnpParamsMap.put("vnp_OrderInfo", orderInfo);
        }else {
            vnpParamsMap.put("vnp_OrderInfo", "No message");
        }
        vnpParamsMap.put("vnp_OrderType", this.orderType);
        vnpParamsMap.put("vnp_Locale", "en");
        vnpParamsMap.put("vnp_ReturnUrl", returnUrl);

        return vnpParamsMap;
    }

    @PostConstruct
    public void init() {
        log.info("Vnp_Pay_Properties init: {}", this);
    }
}
