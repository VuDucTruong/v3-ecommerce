package shop.holy.v3.ecommerce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import shop.holy.v3.ecommerce.shared.property.Vnp_Pay_Properties;
import shop.holy.v3.ecommerce.shared.util.VNPayUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TestApp {
    public static final Vnp_Pay_Properties vnpPayProperties = new Vnp_Pay_Properties();
//
//    public static void main(String[] args) {
//        vnpPayProperties.setVnp_PayUrl("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");
//        vnpPayProperties.setVnp_TmnCode("CBGIJ47H");
//        vnpPayProperties.setSecretKey("H75ZS57DRIGJEW2TQHBD3LZ8EMCRKDCG");
//        vnpPayProperties.setVnp_ReturnUrl("https://www.google.com/");
//        vnpPayProperties.setVnp_Version("2.1.0");
//        vnpPayProperties.setVnp_Command("pay");
//        vnpPayProperties.setOrderType("other");
//
//        String bankCode = "NCB";
//        BigDecimal amount = new BigDecimal("10000");
//        Random random = new Random();
//        var val = random.nextInt(1000000000);
//        String transRef = String.valueOf(val);
//        String orderInfo = "ABCDEF";
//        String ip = "127.0.0.1";
//
//        String vnp_PayUrl = build_Pay_Url("NCB", amount, transRef, orderInfo, ip);
//        log.info("vnp_PayUrl: {}", vnp_PayUrl);
//    }
//
//    public static String build_Pay_Url(String bankCode, BigDecimal amount, String transRef, String orderInfo, String ip) {
//        Map<String, String> vnpParamsMap = vnpPayProperties.buildParamsMap(transRef, orderInfo);
//        vnpParamsMap.put("vnp_Amount", String.valueOf(amount.longValue() * 100));
////        if (bankCode != null && !bankCode.isEmpty()) {
////        }
//            vnpParamsMap.put("vnp_BankCode",bankCode );
//        vnpParamsMap.put("vnp_IpAddr", ip);
//
//        //build query url
//        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
//        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
//        String vnpSecureHash = VNPayUtil.hmacSHA512(vnpPayProperties.getSecretKey(), hashData);
//        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
//        return vnpPayProperties.getVnp_PayUrl() + "?" + queryUrl;
//    }

    public static void main1(String[] args) throws JsonProcessingException {
//        String[] strs = {"abc,", "def,'efg'"};
        long nums[] = {1, 2, 3, 4, 5};
        System.out.println(nums.toString());
        String result = Arrays.stream(nums)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));

        log.info("----------------");
        log.info("{}", result);
        log.info("----------------");
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        LocalDate localDate = LocalDate.now();
        String str = localDate.toString();
        LocalDate test = LocalDate.parse(str);
        log.info("{}", test);
    }
}
