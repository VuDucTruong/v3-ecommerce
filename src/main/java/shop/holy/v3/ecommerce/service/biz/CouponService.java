package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.coupon.RequestCouponCreate;
import shop.holy.v3.ecommerce.api.dto.coupon.RequestCouponSearch;
import shop.holy.v3.ecommerce.api.dto.coupon.RequestCouponUpdate;
import shop.holy.v3.ecommerce.api.dto.coupon.ResponseCoupon;
import shop.holy.v3.ecommerce.persistence.entity.Coupon;
import shop.holy.v3.ecommerce.persistence.repository.ICouponRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.CouponType;
import shop.holy.v3.ecommerce.shared.exception.ResourceNotFoundException;
import shop.holy.v3.ecommerce.shared.mapper.CouponMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final ICouponRepository couponRepository;
    private final CouponMapper couponMapper;

    public ResponsePagination<ResponseCoupon> search(RequestCouponSearch searchReq) {
        Specification<Coupon> spec = couponMapper.fromRequestToSpecification(searchReq);
        Pageable pageable = couponMapper.fromRequestPageableToPageable(searchReq.pageRequest());
        Page<Coupon> coupons = couponRepository.findAll(spec, pageable);
        Page<ResponseCoupon> responses = coupons.map(couponMapper::fromEntityToResponse);
        return ResponsePagination.fromPage(responses);
    }

    public Pair<Coupon, BigDecimal> evaluateDiscount(BigDecimal amount, String code) {
        Coupon coupon = couponRepository.updateCouponUsage(code)
                .orElseThrow(() -> new ResourceNotFoundException("COUPON NOT FOUND OR EXPIRED"));
        if (coupon.getType().equals(CouponType.PERCENTAGE.name())) {
            BigDecimal discount = amount.multiply(coupon.getValue().divide(BigDecimal.valueOf(100)));
            amount = amount.subtract(discount);
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                return new Pair<>(coupon, BigDecimal.ZERO);
            }
            return new Pair<>(coupon, amount);
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            return new Pair<>(coupon, BigDecimal.ZERO);
        }
        return new Pair<>(coupon, amount.subtract(coupon.getValue()));
    }

    public ResponseCoupon findByCode(String code) {
        Coupon c = couponRepository.findByCode(code)
                .orElseThrow(BizErrors.INVALID_COUPON::exception);
        return couponMapper.fromEntityToResponse(c);
    }
    public ResponseCoupon findById(long id){
        Coupon c = couponRepository.findById(id)
                .orElseThrow(BizErrors.INVALID_COUPON::exception);
        return couponMapper.fromEntityToResponse(c);
    }


    @Transactional
    public ResponseCoupon insert(RequestCouponCreate request) {
        Coupon coupon = couponMapper.fromRequestCreateToEntity(request);
        return couponMapper.fromEntityToResponse(couponRepository.save(coupon));
    }

    @Transactional
    public ResponseCoupon update(RequestCouponUpdate request) throws IOException {
        Coupon coupon = couponMapper.fromRequestUpdateToEntity(request);
        return couponMapper.fromEntityToResponse(couponRepository.save(coupon));
    }

    public int deleteCoupon(UUID id, boolean isSoft) {
        if (isSoft) {
            return couponRepository.updateDeletedAtById(id);
        }
        return couponRepository.deleteCouponById(id);
    }

}
