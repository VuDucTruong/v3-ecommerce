package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.payment.RequestPaymentCallback;
import shop.holy.v3.ecommerce.api.dto.payment.RequestPaymentUrl;
import shop.holy.v3.ecommerce.api.dto.payment.ResponsePayment;
import shop.holy.v3.ecommerce.service.biz.order.PaymentCommand;
import shop.holy.v3.ecommerce.shared.util.VNPayUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("payments")
@Tag(name = "Payments")
public class ControllerPayment {

    private final PaymentCommand paymentCommand;

    @PutMapping("urls")
    @Operation(summary = "step 2: get vnpay url. Pre_Cond: must create Order first")
    public String getPaymentById(@RequestBody RequestPaymentUrl requestPaymentUrl, HttpServletRequest httpServletRequest) {
        var ip = VNPayUtil.getIpAddress(httpServletRequest);
        return paymentCommand.upsertVnpPayment(requestPaymentUrl, ip);
    }

    @Operation(summary = "step 3: forward callbacks query parameters. Pre_Cond: got payment url, performed payment on vnp page")
    @PutMapping("vnpay")
    public ResponsePayment vnpayPayment(@RequestBody RequestPaymentCallback req) {
        return paymentCommand.callBackPayment(req);
    }

    @Operation(summary = "cần Endpoint này k vậy? vì 1 order gắn với 1 payment",
            description = "#muốn xem payments -> check thanh toán ở link sandbox")
    @GetMapping(value = "history", produces = MediaType.TEXT_PLAIN_VALUE)
    public String gethistory(@RequestParam(required = false) Long id) {
        return """
                Đéo có đâu, 1 order = 1 payment
                => đi qua lấy order luôn bao gồm -> Optional: payment
                """;
    }

}
