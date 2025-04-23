package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.coupon.RequestCouponCreate;
import shop.holy.v3.ecommerce.api.dto.coupon.RequestCouponSearch;
import shop.holy.v3.ecommerce.api.dto.coupon.RequestCouponUpdate;
import shop.holy.v3.ecommerce.api.dto.coupon.ResponseCoupon;
import shop.holy.v3.ecommerce.service.biz.CouponService;
import shop.holy.v3.ecommerce.shared.constant.DefaultValues;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Coupons")
@RequestMapping("coupons")
public class ControllerCoupon {

    private final CouponService couponService;

    @PostMapping("/searches")
    public ResponseEntity<?> getAllCoupons(@RequestBody RequestCouponSearch searchReq) {
        ResponsePagination<ResponseCoupon> res = couponService.search(searchReq);
        return ResponseEntity.ok(res);
    }

    @GetMapping()
    public ResponseEntity<?> getById(@RequestParam(required = false, defaultValue = DefaultValues.ID + "") long id,
                                     @RequestParam(required = false) String productKey,
                                     @RequestParam(required = false) boolean deleted) {
        return ResponseEntity.ok(couponService.findByIdentitfier(id, productKey, deleted));
    }


    @PostMapping(value = "")
    public ResponseEntity<?> createCoupon(@RequestBody RequestCouponCreate request) {
        return ResponseEntity.ok(couponService.insert(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCouponById(
            @PathVariable UUID id, @RequestParam(required = false) boolean isSoft) {
        return ResponseEntity.ok(couponService.deleteCoupon(id, isSoft));
    }

    @PutMapping(value = "")
    public ResponseEntity<?> updateCoupon(@RequestBody RequestCouponUpdate request) throws IOException {
        return ResponseEntity.ok(couponService.update(request));
    }

}
