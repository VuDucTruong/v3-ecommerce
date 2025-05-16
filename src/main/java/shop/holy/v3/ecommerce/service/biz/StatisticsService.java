package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatTotal;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatistics;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_Date_Revenue;
import shop.holy.v3.ecommerce.persistence.repository.*;
import shop.holy.v3.ecommerce.shared.constant.OrderStatus;
import shop.holy.v3.ecommerce.shared.constant.RoleEnum;
import shop.holy.v3.ecommerce.shared.util.AppDateUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatistics.*;

@RequiredArgsConstructor
@Service
public class StatisticsService {
    private final IAccountRepository accountRepository;
    private final IOrderRepository orderRepository;
    private final IOrderDetailRepository orderDetailRepository;

    public CompletableFuture<ResponseStatTotal> getStatTotal(int backDayTotals) {
        Date sevenDaysAgoMidnight = AppDateUtils.backDateByDays(backDayTotals);
        final Date now = new Date();

        CompletableFuture<Integer> cusCount = CompletableFuture.supplyAsync(() -> accountRepository.countAccountByRole(RoleEnum.CUSTOMER.name()));
        CompletableFuture<Long> soldCount = CompletableFuture.supplyAsync(() -> {
            return orderDetailRepository.findSumQuantityByRecentTime(sevenDaysAgoMidnight, now);
        });
        CompletableFuture<BigDecimal> revenue = CompletableFuture.supplyAsync(() -> {
            return orderRepository.findSumTotalByRecentTime(OrderStatus.COMPLETED.name(), sevenDaysAgoMidnight, now);
        });
        CompletableFuture<Long> orderCount = CompletableFuture.supplyAsync(() -> {
            return orderRepository.countByStatusAndCreatedAtLessThanAndCreatedAtGreaterThan(OrderStatus.COMPLETED.name(), sevenDaysAgoMidnight, now);
        });

        return CompletableFuture.allOf(cusCount, soldCount, revenue, orderCount)
                .thenApply(v -> new ResponseStatTotal(cusCount.join(), soldCount.join(), revenue.join(), orderCount.join()));
    }

    public CompletableFuture<List<ResposneStatRevenues>> getRevenues(int backDateOffset) {
        Date nDaysAgoMidnight = AppDateUtils.backDateByDays(backDateOffset);
        return CompletableFuture.supplyAsync(() -> {
            List<ProQ_Date_Revenue> revenues = orderRepository.findRevenues(OrderStatus.COMPLETED.name(), nDaysAgoMidnight, new Date());
            return revenues.stream().map(r1 -> new ResposneStatRevenues(r1.getDate(), r1.getRevenue())).toList();
        });
    }

    public CompletableFuture<ResponseStatistics> getStats(int backDayTotals, int backDayRevenues) {
        CompletableFuture<ResponseStatTotal> totals = getStatTotal(backDayTotals);
        CompletableFuture<List<ResposneStatRevenues>> revenues = getRevenues(backDayRevenues);
        return CompletableFuture.allOf(totals, revenues)
                .thenApply(v -> new ResponseStatistics(totals.join(), revenues.join()));
    }

}
