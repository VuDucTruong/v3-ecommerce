package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.holy.v3.ecommerce.api.dto.statistic.RequestFromTo;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatTotal;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatsOrders;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatsProductTrend;
import shop.holy.v3.ecommerce.service.biz.StatisticsQuery;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@RestController
@Tag(name = "Statistics", description = "=> for users home page")
@RequiredArgsConstructor
@RequestMapping("statistics")
public class ControllerStatistics {
    private final StatisticsQuery statisticsQuery;
    private final Random r = new Random();


    @GetMapping("totals")
    public CompletableFuture<ResponseEntity<ResponseStatTotal>> getResponseStatTotal(@ParameterObject RequestFromTo fromTo) {
        if (true)
            return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(new ResponseStatTotal(r.nextInt(100),
                    BigDecimal.valueOf(Math.random()* 1_000_000),
                    BigDecimal.valueOf(Math.random()* 1_000_000), r.nextInt(0, 100))));

        return statisticsQuery.getStatTotal(fromTo).thenApply(ResponseEntity::ok);
    }

    @GetMapping("orders")
    @Operation(summary = "get orders counts from date to date", description = """
            'from' is required
            'to' is optional => default == now
            """)
    public ResponseEntity<Collection<ResponseStatsOrders>> getStatsOrders(@ParameterObject RequestFromTo fromTo) {
        int gap = fromTo.to().getDayOfYear() - fromTo.from().getDayOfYear() + 1;
        LocalDate to = fromTo.to();
        if (true) {
            var temp = IntStream.range(0, gap)
                    .mapToObj(i -> new ResponseStatsOrders(to.minusDays(i), r.nextInt(50), r.nextInt(50), r.nextInt(50), r.nextInt(50)))
                    .toList();
            return ResponseEntity.ok(temp);
        }

        var rs = statisticsQuery.getStatsOrders(fromTo);
        return ResponseEntity.ok(rs);
    }



}
