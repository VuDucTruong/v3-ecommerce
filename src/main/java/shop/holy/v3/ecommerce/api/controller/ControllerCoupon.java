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

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "Coupons")
@RequestMapping("coupons")
public class ControllerCoupon {

    private final CouponService couponService;

    @PostMapping("/searches")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<ResponsePagination<ResponseCoupon>> getAllCoupons(@RequestBody RequestCouponSearch searchReq) {
        ResponsePagination<ResponseCoupon> res = couponService.search(searchReq);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "get 1", description = """
    1. nếu cho cả id & code sẽ  lấy theo id
    2. còn nếu cho 1 trong 2 thì sẽ lấy tương ứng
    3. Lưu ý: code ở đây <b>KHÔNG</b> phải phép LIKE, là phép <b>=</b>
    4. Dùng để check nếu coupon hợp lệ
    ====> 
            """)
    @GetMapping()
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<ResponseCoupon> getByIdentifier(@RequestParam(required = false) Long id,
                                             @RequestParam(required = false) String code,
                                             @RequestParam(required = false) boolean deleted) {
        return ResponseEntity.ok(couponService.findByIdentitfier(id, code, deleted));
    }


    @Operation(summary = "create 1")
    @PostMapping(value = "")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<ResponseCoupon> createCoupon(@RequestBody RequestCouponCreate request) {
        return ResponseEntity.ok(couponService.insert(request));
    }

    @Operation(summary = "delete 1")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<Integer> deleteCouponById(
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
    public ResponseEntity<ResponseCoupon> updateCoupon(@RequestBody RequestCouponUpdate request) throws IOException {
        return ResponseEntity.ok(couponService.update(request));
    }

}
