package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.order.RequestOrderCreate;
import shop.holy.v3.ecommerce.api.dto.order.RequestOrderSearch;
import shop.holy.v3.ecommerce.api.dto.order.ResponseOrder;
import shop.holy.v3.ecommerce.persistence.entity.Coupon;
import shop.holy.v3.ecommerce.persistence.entity.Order;
import shop.holy.v3.ecommerce.persistence.entity.Product;
import shop.holy.v3.ecommerce.persistence.repository.IOrderRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProductRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;
import shop.holy.v3.ecommerce.shared.exception.UnAuthorisedException;
import shop.holy.v3.ecommerce.shared.mapper.OrderMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static shop.holy.v3.ecommerce.api.dto.order.RequestOrderCreate.RequestOrderDetail;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final CouponService couponService;


    public ResponseOrder findByCode(long id, boolean deleted) {
        Order order;
        if (deleted) {
            return orderRepository.findFirstByIdEqualsAndDeletedAtIsNull(id)
                    .map(orderMapper::fromEntityToResponse)
                    .orElseThrow(() -> BizErrors.ORDER_NOT_FOUND.exception());
        }
        order = orderRepository.findById(id)
                .orElseThrow(() -> BizErrors.ORDER_NOT_FOUND.exception());

//        List<Long> productIds = order.getOrderDetails().stream()
//                .map(OrderDetail::getProductId)
//                .toList();
//        Map<Long, Product> productMap = productRepository.findAllByIdIn(productIds).stream()
//                .collect(Collectors.toMap(Product::getId, product -> product));
//        order.getOrderDetails().forEach(detail -> detail.setProduct(
//                productMap.getOrDefault(detail.getProductId(), null)
//        ));
        return orderMapper.fromEntityToResponse(order);
    }

    public ResponsePagination<ResponseOrder> search(RequestOrderSearch searchReq) {
        Specification<Order> specs = orderMapper.fromRequestSearchToSpec(searchReq);
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        if (Objects.equals(authAccount.getRole(), RoleEnum.CUSTOMER)) {
            specs = specs.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("accountId"), authAccount.getId()));
        } else throw new UnAuthorisedException("UNAUTHORIZED");
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(searchReq.pageRequest());
        Page<Order> orders = orderRepository.findAll(specs, pageable);

        return ResponsePagination.fromPage(orders.map(order -> {
            OrderStatus status;
            if (order.getPayment() == null)
                return orderMapper.fromEntityToResponseWithStatus(order, OrderStatus.PENDING);
            status = orderMapper.fromPaymentStatusToOrderStatus(order.getPayment().getStatus());
            if (status != null) {
                return orderMapper.fromEntityToResponseWithStatus(order, status);
            }
            return orderMapper.fromEntityToResponse(order);
        }));
    }


    @Transactional
    public ResponseOrder insert(RequestOrderCreate request) {

        Map<Long, Integer> productQuantities = request.orderDetails().stream()
                .collect(Collectors.toMap(RequestOrderDetail::productId, RequestOrderDetail::quantity));
        List<Long> productIds = new ArrayList<>(productQuantities.keySet());
        Collections.sort(productIds);

        List<Product> products = productRepository.findProductsByIdIn(productIds);
        if (products.size() != productIds.size()) {
            throw BizErrors.PRODUCT_NOT_FOUND.exception();
        }

        ///  SUM(Price X quantities,...)
        BigDecimal amount = products.stream()
                .map(product -> product.getPrice()
                        .multiply(BigDecimal.valueOf(productQuantities.get(product.getId())))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = orderMapper.fromCreateRequestToEntity(request);
        if (request.couponCode() != null) {
            Pair<Coupon, BigDecimal> couponResult = couponService.evaluateDiscount(amount, request.couponCode());
            amount = couponResult.b;
            order.setCouponId(couponResult.a.getId());
        }

        order.setTotal(amount);
        Order result = orderRepository.findById(order.getId())
                .orElseThrow(BizErrors.ORDER_NOT_FOUND::exception);
        return orderMapper.fromEntityToResponse(result);
    }

    @Transactional
    public int deleteOrderById(Long id) {
        return orderRepository.updateOrderDeletedAt(id);
    }


}
