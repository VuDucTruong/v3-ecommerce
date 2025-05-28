package shop.holy.v3.ecommerce.service.biz.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.coupon.RequestCouponSearch;
import shop.holy.v3.ecommerce.api.dto.coupon.ResponseCoupon;
import shop.holy.v3.ecommerce.persistence.entity.Coupon;
import shop.holy.v3.ecommerce.persistence.repository.ICouponRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.mapper.CouponMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;


@RequiredArgsConstructor
@Service
public class CouponQuery {
    private final ICouponRepository couponRepository;
    private final CouponMapper couponMapper;

    public ResponsePagination<ResponseCoupon> search(RequestCouponSearch searchReq) {
        Specification<Coupon> spec = couponMapper.fromRequestToSpecification(searchReq);
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(searchReq.pageRequest());
        Page<Coupon> coupons = couponRepository.findAll(spec, pageable);
        Page<ResponseCoupon> responses = coupons.map(couponMapper::fromEntityToResponse);
        return ResponsePagination.fromPage(responses);
    }


    public ResponseCoupon findByIdentitfier(Long id, String code, boolean deleted) {
        Coupon c;
        if (id != null)
            if (deleted)
                c = couponRepository.findById(id).orElseThrow(BizErrors.INVALID_COUPON::exception);
            else
                c = couponRepository.findFirstByIdAndDeletedAtIsNull(id).orElseThrow(BizErrors.INVALID_COUPON::exception);
        else {
            if (deleted)
                c = couponRepository.findFirstByCode(code).orElseThrow(BizErrors.INVALID_COUPON::exception);
            else
                c = couponRepository.findFirstByCodeAndDeletedAtIsNull(code).orElseThrow(BizErrors.INVALID_COUPON::exception);
        }

        return couponMapper.fromEntityToResponse(c);

    }


}
