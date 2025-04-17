package shop.holy.v3.ecommerce.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class ControllerTest
{
    @GetMapping("/biz-errors")
    public ResponseEntity<?> testBizErrors() {
        throw BizErrors.COUPON_ALREADY_USED.exception();
    }

}
