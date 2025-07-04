package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.statistic.RequestFromTo;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatTotal;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatsOrders;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatsProductTrend;
import shop.holy.v3.ecommerce.persistence.entity.Order;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_Date_Revenue;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_Sold_ProdId_ProdName;
import shop.holy.v3.ecommerce.persistence.repository.IAccountRepository;
import shop.holy.v3.ecommerce.persistence.repository.IOrderDetailRepository;
import shop.holy.v3.ecommerce.persistence.repository.IOrderRepository;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;
import shop.holy.v3.ecommerce.shared.util.AppDateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatistics.ResponseStatRevenues;

@RequiredArgsConstructor
@Service
public class StatisticsQuery {
    private final IAccountRepository accountRepository;
    private final IOrderRepository orderRepository;
    private final IOrderDetailRepository orderDetailRepository;

    public CompletableFuture<ResponseStatTotal> getStatTotal(RequestFromTo fromTo) {
        Date from = AppDateUtils.toStartOfDay(fromTo.from());
        Date to = AppDateUtils.toEndOfDay(fromTo.to());

        CompletableFuture<Integer> totalNewCustomers = CompletableFuture.supplyAsync(
                () -> accountRepository.countAccountByRoleAndCreatedAtAfterAndCreatedAtBefore(RoleEnum.CUSTOMER.name(), from, to));
        CompletableFuture<Long> totalOrders = CompletableFuture.supplyAsync(() -> {
            return orderRepository.countByStatusAndCreatedAtAfterAndCreatedAtBefore(OrderStatus.COMPLETED.name(), from, to);
        });
        CompletableFuture<BigDecimal> revenue = CompletableFuture.supplyAsync(() -> {
            return orderRepository.findSumTotalByRecentTime(OrderStatus.COMPLETED.name(), from, to).orElse(BigDecimal.ZERO);
        });


        return CompletableFuture.allOf(totalNewCustomers, revenue, totalOrders)
                .thenApply(v -> {
                    var average = revenue.join().divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
                    return new ResponseStatTotal(totalNewCustomers.join(), average, revenue.join(), totalOrders.join());
                });
    }

    public CompletableFuture<List<ResponseStatRevenues>> getRevenues(int backDateOffset) {
        Date nDaysAgoMidnight = AppDateUtils.backDateByDays(backDateOffset);
        return CompletableFuture.supplyAsync(() -> {
            List<ProQ_Date_Revenue> revenues = orderRepository.findRevenues(OrderStatus.COMPLETED.name(), nDaysAgoMidnight, new Date());
            return revenues.stream().map(r1 -> new ResponseStatRevenues(r1.getDate(), r1.getRevenue())).toList();
        });
    }

    public Collection<ResponseStatsOrders> getStatsOrders(RequestFromTo fromTo) {
        Date from = AppDateUtils.toStartOfDay(fromTo.from());
        Date to = AppDateUtils.toEndOfDay(fromTo.to());

        List<Order> orders = orderRepository.findAllByCreatedAtAfterAndCreatedAtBefore(from, to);
        Map<LocalDate, ResponseStatsOrders> items = orders.stream().collect(
                Collectors.<Order, LocalDate, ResponseStatsOrders>toMap(o -> AppDateUtils.toLocalDate(o.getCreatedAt()),
                        o -> new ResponseStatsOrders(AppDateUtils.toLocalDate(o.getCreatedAt()), o.getStatus()),
                        (stat1, stat2) -> {
                            stat1.add(stat2);
                            return stat1;}
                )
        );
        return items.values();
    }

    public Collection<ResponseStatsProductTrend> getProductTrends(RequestFromTo fromTo, int size) {
        Date from = AppDateUtils.toStartOfDay(fromTo.from());
        Date to = AppDateUtils.toEndOfDay(fromTo.to());
        Pageable pageable = PageRequest.of(0, size, Sort.unsorted());
        List<ProQ_Sold_ProdId_ProdName> quantitiesWithMeta = orderDetailRepository.findSumQuantityByRecentTime(from, to, pageable);
        var result = quantitiesWithMeta.stream().map(q ->
                        new ResponseStatsProductTrend(q.getTotalSold(), new ResponseStatsProductTrend.ProductTrend(q.getProdId(), q.getProdName())))
                .toList();
        return result;
    }


}
