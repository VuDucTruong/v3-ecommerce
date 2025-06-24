//package shop.holy.v3.ecommerce;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
//import org.springframework.boot.context.properties.bind.Bindable;
//import org.springframework.boot.context.properties.bind.Binder;
//import org.springframework.core.env.Environment;
//import org.springframework.core.env.MutablePropertySources;
//import org.springframework.core.env.PropertiesPropertySource;
//import org.springframework.core.env.StandardEnvironment;
//import org.springframework.core.io.ClassPathResource;
//import shop.holy.v3.ecommerce.service.cloud.CloudinaryService;
//import shop.holy.v3.ecommerce.shared.property.CloudinaryProperties;
//import shop.holy.v3.ecommerce.shared.util.VNPayUtil;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.time.LocalDate;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//public class TestApp {
//    //    public static final Vnp_Pay_Properties vnpPayProperties = new Vnp_Pay_Properties();
////    public static void main(String[] args) throws Exception {
////        String dateString = "20250509091544";
////        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
////        Date date = sdf.parse(dateString);
////        System.out.println(date);
////    }
//    public static void main(String[] args) {
//        String str = "vnp_Amount=500000000&vnp_Command=pay&vnp_CreateDate=20250614230945&vnp_CurrCode=VND&vnp_ExpireDate=20250614232445&vnp_IpAddr=0%3A0%3A0%3A0%3A0%3A0%3A0%3A1&vnp_Locale=en&vnp_OrderInfo=ok+this+is+good&vnp_OrderType=other&vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A3000%2Fcart%2Fpayment&vnp_TmnCode=ETGKRGNL&vnp_TxnRef=173e9257-9621-4874-a81e-58323580bb13&vnp_Version=2.1.0";
//        String secret = "U3Z57PNITUDWZU85HA03S1NNMY1NOFUV";
//        String vnpSecureHash = VNPayUtil.hmacSHA512(secret, str);
//        System.out.println( vnpSecureHash);
//
//    }
//    public static void main1231323223(String[] args) {
//        Map<String,String> temp = new HashMap<>();
//        temp.put("username","admin");
//        temp.put("password","admin");
//        var temps = new Map[2];
//        temps[0] = temp;
//        temps[1] = temp;
//        System.out.println(Arrays.toString(temps));
//    }
//
//    public static void main123123(String[] args) {
//        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
//        yaml.setResources(new ClassPathResource("secret/cloudinary.yaml"));
//        Properties props = yaml.getObject();
//
//        Environment env = new StandardEnvironment();
//        MutablePropertySources sources = ((StandardEnvironment) env).getPropertySources();
//        sources.addFirst(new PropertiesPropertySource("cloudinary", props));
//
//        Binder binder = Binder.get(env);
//        CloudinaryProperties config = binder.bind("", Bindable.of(CloudinaryProperties.class)).get();
//        CloudinaryService cloudinaryService = new CloudinaryService(config);
//        var str = cloudinaryService.extractPublicId(
//                "https://res.cloudinary.com/dm45tt6nt/image/upload/v1747070010/ecommerce/product/8ea092fb-c495-4f43-b7cb-26c5ef20b8a9.png",
//                "/ecommerce/product"
//        );
//        log.info(str);
//        var cd = cloudinaryService.getCloudinary();
////        log.info("url: {}", );
//
//
////        System.out.println(config.getCloudName());
//
//
//    }
//
//    public static String extractPublicId(String cloudinaryUrl) {
//        try {
//            URL url = new URL(cloudinaryUrl);
//            String path = url.getPath();
//            List<String> pathSegments = Arrays.asList(path.split("/"));
//
//
//            int uploadIndex = pathSegments.indexOf("upload");
//            if (uploadIndex == -1 || uploadIndex + 2 >= pathSegments.size()) {
//                throw new IllegalArgumentException("Invalid Cloudinary URL format");
//            }
//
//
//            String publicIdWithExtension = pathSegments.get(uploadIndex + 2);
//
//
//            String publicId = publicIdWithExtension.substring(0, publicIdWithExtension.lastIndexOf('.'));
//
//            return publicId;
//        } catch (MalformedURLException e) {
//            throw new IllegalArgumentException("Invalid URL", e);
//        }
//    }
//
//
////
////    public static void main(String[] args) {
////        vnpPayProperties.setVnp_PayUrl("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");
////        vnpPayProperties.setVnp_TmnCode("CBGIJ47H");
////        vnpPayProperties.setSecretKey("H75ZS57DRIGJEW2TQHBD3LZ8EMCRKDCG");
////        vnpPayProperties.setVnp_ReturnUrl("https://www.google.com/");
////        vnpPayProperties.setVnp_Version("2.1.0");
////        vnpPayProperties.setVnp_Command("pay");
////        vnpPayProperties.setOrderType("other");
////
////        String bankCode = "NCB";
////        BigDecimal amount = new BigDecimal("10000");
////        Random random = new Random();
////        var val = random.nextInt(1000000000);
////        String transRef = String.valueOf(val);
////        String orderInfo = "ABCDEF";
////        String ip = "127.0.0.1";
////
////        String vnp_PayUrl = build_Pay_Url("NCB", amount, transRef, orderInfo, ip);
////        log.info("vnp_PayUrl: {}", vnp_PayUrl);
////    }
////
////    public static String build_Pay_Url(String bankCode, BigDecimal amount, String transRef, String orderInfo, String ip) {
////        Map<String, String> vnpParamsMap = vnpPayProperties.buildParamsMap(transRef, orderInfo);
////        vnpParamsMap.put("vnp_Amount", String.valueOf(amount.longValue() * 100));
//
//    /// /        if (bankCode != null && !bankCode.isEmpty()) {
//    /// /        }
////            vnpParamsMap.put("vnp_BankCode",bankCode );
////        vnpParamsMap.put("vnp_IpAddr", ip);
////
////        //build query url
////        String payUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
////        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
////        String vnpSecureHash = VNPayUtil.hmacSHA512(vnpPayProperties.getSecretKey(), hashData);
////        payUrl += "&vnp_SecureHash=" + vnpSecureHash;
////        return vnpPayProperties.getVnp_PayUrl() + "?" + payUrl;
////    }
//    public static void main1(String[] args) throws JsonProcessingException {
////        String[] strs = {"abc,", "def,'efg'"};
//        long nums[] = {1, 2, 3, 4, 5};
//        System.out.println(nums.toString());
//        String result = Arrays.stream(nums)
//                .mapToObj(String::valueOf)
//                .collect(Collectors.joining(","));
//
//        log.info("----------------");
//        log.info("{}", result);
//        log.info("----------------");
//        ObjectMapper mapper = new ObjectMapper();
//        Date date = new Date();
//        LocalDate localDate = LocalDate.now();
//        String str = localDate.toString();
//        LocalDate test = LocalDate.parse(str);
//        log.info("{}", test);
//    }
//}
