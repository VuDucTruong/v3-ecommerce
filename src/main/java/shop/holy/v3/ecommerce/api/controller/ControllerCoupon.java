package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<?> getAllCoupons(@RequestBody RequestCouponSearch searchReq) {
        ResponsePagination<ResponseCoupon> res = couponService.search(searchReq);
        return ResponseEntity.ok(res);
    }


    @Operation(summary = "get 1")
    @GetMapping()
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<?> getByIdentifier(@RequestParam(required = false) Long id,
                                             @RequestParam(required = false) String code,
                                             @RequestParam(required = false) boolean deleted) {
        return ResponseEntity.ok(couponService.findByIdentitfier(id, code, deleted));
    }


    @Operation(summary = "create 1")
    @PostMapping(value = "")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<?> createCoupon(@RequestBody RequestCouponCreate request) {
        return ResponseEntity.ok(couponService.insert(request));
    }

    @Operation(summary = "delete 1")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<?> deleteCouponById(
            @PathVariable long id, @RequestParam(required = false) boolean isSoft) {
        return ResponseEntity.ok(couponService.deleteCoupon(id));
    }

    @Operation(summary = "delete many")
    @DeleteMapping("")
    public ResponseEntity<Integer> deleteMany(@RequestParam long[] ids) {
        return ResponseEntity.ok(couponService.deleteCouponsIn(ids));
    }

    @Operation(summary = "update 1")

    @PutMapping(value = "")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<?> updateCoupon(@RequestBody RequestCouponUpdate request) throws IOException {
        return ResponseEntity.ok(couponService.update(request));
    }

}
