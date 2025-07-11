package shop.holy.v3.ecommerce.service.biz.order;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.order.RequestOrderCreate;
import shop.holy.v3.ecommerce.api.dto.order.ResponseOrder;
import shop.holy.v3.ecommerce.persistence.entity.Coupon;
import shop.holy.v3.ecommerce.persistence.entity.Order;
import shop.holy.v3.ecommerce.persistence.entity.OrderDetail;
import shop.holy.v3.ecommerce.persistence.entity.product.Product;
import shop.holy.v3.ecommerce.persistence.repository.ICouponRepository;
import shop.holy.v3.ecommerce.persistence.repository.IOrderDetailRepository;
import shop.holy.v3.ecommerce.persistence.repository.IOrderRepository;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.CouponType;
import shop.holy.v3.ecommerce.shared.mapper.OrderMapper;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderCommand {
    private final ICouponRepository couponRepository;
    private final IOrderRepository orderRepository;
    private final IOrderDetailRepository detailRepository;
    private final IProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public ResponseOrder insert(RequestOrderCreate request) {
        Map<Long, OrderDetail> prodId_to_OrderDetail = orderMapper.fromRequestToOrderDetails(request.orderDetails());
        Set<Product> products = productRepository.findProductsByIdIn(prodId_to_OrderDetail.keySet());
        if (products.size() != prodId_to_OrderDetail.size())
            throw BizErrors.PRODUCT_NOT_FOUND.exception();
        ///  SUM(Price X quantities,...)
        BigDecimal originalAmount = evalProducts_And_caculate_OriginalPrice(products, prodId_to_OrderDetail);
        Order order = this.calculateOrder(request, originalAmount);

        ///  SAVE ORDER FIRST
        Order result = orderRepository.save(order);

        ///  NOW SAVE Order details
        Collection<OrderDetail> details = save_Details(result, products, prodId_to_OrderDetail);
        return orderMapper.fromEntityToResponse_InDetail(result, null, null, details);
    }

    private BigDecimal evalProducts_And_caculate_OriginalPrice(Collection<Product> products, Map<Long, OrderDetail> prodId_to_OrderDetail) {
        return products.stream()
                .map(product -> product.getPrice()
                        .multiply(BigDecimal.valueOf(prodId_to_OrderDetail.get(product.getId()).getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order calculateOrder(RequestOrderCreate request, BigDecimal originalAmount) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        long profileId = authAccount.getProfileId();

        Order order = orderMapper.fromCreateRequestToEntity(request);
        order.setProfileId(profileId);


        if (request.couponCode() != null) {
            Pair<Coupon, BigDecimal> couponResult = this.evaluateDiscount(originalAmount, request.couponCode());
            order.setCouponId(couponResult.a.getId());
            order.setAmount(couponResult.b);
        } else
            order.setAmount(originalAmount);
        order.setOriginalAmount(originalAmount);
        return order;
    }

    private Collection<OrderDetail> save_Details(Order order, Collection<Product> products, Map<Long, OrderDetail> prodId_by_OrderDetail) {
        for (Product product : products) {
            OrderDetail detail = prodId_by_OrderDetail.get(product.getId());
            detail.setOriginalPrice(product.getOriginalPrice());
            /// productId & quantity already set in mapper
            detail.setPrice(product.getPrice());
            detail.setOrderId(order.getId());
            detail.setProduct(product);
        }
        Collection<OrderDetail> orderDetails = prodId_by_OrderDetail.values();
        detailRepository.saveAll(orderDetails);
        return orderDetails;
    }

    private Pair<Coupon, BigDecimal> evaluateDiscount(BigDecimal amount, String code) {
        Coupon coupon = couponRepository.updateCouponUsage(code)
                .orElseThrow(BizErrors.INVALID_COUPON::exception);

        BigDecimal appliedDiscount;
        if (coupon.getType().equals(CouponType.PERCENTAGE.name())) {
            appliedDiscount = amount.multiply(coupon.getValue().divide(BigDecimal.valueOf(100)));
        } else if (coupon.getType().equals(CouponType.FIXED.name())) {
            appliedDiscount = coupon.getValue();
        } else {
            throw BizErrors.INVALID_COUPON.exception();
        }

        if (appliedDiscount.compareTo(amount) > 0) {
            appliedDiscount = amount; // Ensure no negative total
        }

        BigDecimal result = amount.subtract(appliedDiscount);

        return new Pair<>(coupon, result.max(BigDecimal.ZERO)); // Final safety net
    }


    @Transactional
    public int deleteOrderById(Long id) {
        return orderRepository.updateOrderDeletedAt(id);
    }

    @Transactional
    public int deleteOrders(long[] ids) {
        if (ids.length > 0)
            return orderRepository.updateOrderDeletedAtByIdIn(ids);
        return 0;
    }

}
