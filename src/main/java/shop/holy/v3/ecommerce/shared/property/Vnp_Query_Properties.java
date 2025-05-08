//package shop.holy.v3.ecommerce.shared.property;
//
//
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.bind.Name;
//import org.springframework.context.annotation.Configuration;
//import shop.holy.v3.ecommerce.shared.constant.VnpKeys;
//
//import java.util.Map;
//import java.util.UUID;
//
//@Getter
//@Setter
//@Configuration
//@ConfigurationProperties(prefix = "payment.query")
//public class Vnp_Query_Properties {
//    @Name("url")
//    private String vnp_QueryUrl;
//    @Name("tmnCode")
//    private String vnp_TmnCode;
//    @Name("secretKey")
//    private String secretKey;
//    @Name("version")
//    private String vnp_Version;
//    @Name("command")
//    private String vnp_Command;
//
//    public Map<String, String> buildParamsMap(String transRef, String orderInfo) {
//        Map<String, String> vnpParamsMap = new java.util.HashMap<>();
//        vnpParamsMap.put(VnpKeys.vnp_RequestId, UUID.randomUUID().toString());
//        vnpParamsMap.put("vnp_Version", this.vnp_Version);
//        vnpParamsMap.put("vnp_Command", this.vnp_Command);
//        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
//        vnpParamsMap.put("vnp_TxnRef", transRef);
//        vnpParamsMap.put("vnp_OrderInfo", orderInfo);
//
//
//        return vnpParamsMap;
//    }
//
//
//}
