package shop.holy.v3.ecommerce.service.biz.coupon;

import lombok.RequiredArgsConstructor;
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
import shop.holy.v3.ecommerce.shared.mapper.CouponMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class CouponCommand {

    private final ICouponRepository couponRepository;
    private final CouponMapper couponMapper;

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

    @Transactional
    public int deleteCoupon(long id) {
        return couponRepository.updateDeletedAtById(id);
    }

    @Transactional
    public int deleteCouponsIn(long[] ids) {
        if (ids == null || ids.length == 0)
            return 0;

        return couponRepository.updateDeletedAtByIdIn(ids);
    }

}
