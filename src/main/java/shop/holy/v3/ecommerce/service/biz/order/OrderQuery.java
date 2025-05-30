package shop.holy.v3.ecommerce.service.biz.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.mail.MailProductKeys;
import shop.holy.v3.ecommerce.api.dto.order.RequestOrderSearch;
import shop.holy.v3.ecommerce.api.dto.order.ResponseOrder;
import shop.holy.v3.ecommerce.persistence.entity.Order;
import shop.holy.v3.ecommerce.persistence.entity.OrderDetail;
import shop.holy.v3.ecommerce.persistence.repository.IOrderDetailRepository;
import shop.holy.v3.ecommerce.persistence.repository.IOrderRepository;
import shop.holy.v3.ecommerce.service.biz.notification.NotificationQuery;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.mapper.OrderMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderQuery {

    private final IOrderRepository orderRepository;
    private final IOrderDetailRepository detailRepository;
    private final OrderMapper orderMapper;
    private final NotificationQuery notificationQuery;

    /**
     * shouldn't be able to find non-owned order
     */
    public CompletableFuture<ResponseOrder> findById(long id, boolean deleted) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        CompletableFuture<Pair<Order, MailProductKeys>> futureOrderAndMail = CompletableFuture.supplyAsync(() -> {
                    if (authAccount.isAdmin() || authAccount.isStaff()) {
                        if (deleted)
                            return orderRepository.findFirstByIdEqualsAndDeletedAtIsNull(id);
                        else
                            return orderRepository.findById(id);
                    } else if (deleted)
                        return orderRepository.findFirstByIdEqualsAndProfileIdEqualsAndDeletedAtIsNull(id, authAccount.getProfileId());
                    else return orderRepository.findById(id);
                })
                .thenApply(o -> o.orElseThrow(BizErrors.ORDER_NOT_FOUND::exception))
                .thenApply(o -> {
                    MailProductKeys successProdKey = notificationQuery.getSentMailMetaByOrderIdAndStatus(id, o.getStatus());
                    return Pair.of(o, successProdKey);
                });
        CompletableFuture<List<OrderDetail>> futureDetails = CompletableFuture.supplyAsync(() -> detailRepository.findAllByOrderId(id));
        return CompletableFuture.allOf(futureOrderAndMail, futureDetails).thenApply(v -> {
            List<OrderDetail> details = futureDetails.join();
            var orderAndMail = futureOrderAndMail.join();
            Order order = orderAndMail.getLeft();
            MailProductKeys mailProductKeys = orderAndMail.getRight();
            return orderMapper.fromEntityToResponse_InDetail(order, mailProductKeys, details);
        });
    }

    public ResponsePagination<ResponseOrder> search(RequestOrderSearch searchReq) {
        Specification<Order> specs = orderMapper.fromRequestSearchToSpec(searchReq);

        Pageable pageable = MappingUtils.fromRequestPageableToPageable(searchReq.pageRequest());
        Page<Order> orders = orderRepository.findAll(specs, pageable);

        return ResponsePagination.fromPage(orders.map(orderMapper::fromEntityToResponse_Light));
    }


}
