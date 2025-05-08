package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.payment.RequestPaymentCallback;
import shop.holy.v3.ecommerce.api.dto.payment.RequestPaymentUrl;
import shop.holy.v3.ecommerce.api.dto.payment.ResponsePayment;
import shop.holy.v3.ecommerce.service.biz.PaymentService;
import shop.holy.v3.ecommerce.shared.util.VNPayUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("payments")
@Tag(name = "Payments")
public class ControllerPayment {

    private final PaymentService paymentService;

    @PutMapping("urls")
    public String getPaymentById(@RequestBody RequestPaymentUrl requestPaymentUrl, HttpServletRequest httpServletRequest) {
        var ip = VNPayUtil.getIpAddress(httpServletRequest);
        return paymentService.upsertVnpPayment(requestPaymentUrl, ip);
    }

    @PutMapping("vnpay")
    public ResponsePayment vnpayPayment(@RequestBody RequestPaymentCallback req) {
        return paymentService.callBackPayment(req);
    }

    @GetMapping(value = "history", produces = MediaType.TEXT_PLAIN_VALUE)
    public String his() {
        return """
                Đéo có đâu, 1 order = 1 payment
                => đi qua lấy order luôn bao gồm -> Optional: payment
                """;
    }

}
