package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.holy.v3.ecommerce.api.dto.statistic.RequestFromTo;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatTotal;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatsOrders;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatsProductTrend;
import shop.holy.v3.ecommerce.service.biz.StatisticsQuery;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@RestController
@Tag(name = "Statistics", description = "=> for users home page")
@RequiredArgsConstructor
@RequestMapping("statistics")
public class ControllerStatistics {
    private final StatisticsQuery statisticsQuery;


    @GetMapping("totals")
    public CompletableFuture<ResponseEntity<ResponseStatTotal>> getResponseStatTotal(@ParameterObject RequestFromTo fromTo) {
        return statisticsQuery.getStatTotal(fromTo).thenApply(ResponseEntity::ok);
    }

    @GetMapping("orders")
    @Operation(summary = "get orders counts from date to date", description = """
            'from' is required
            'to' is optional => default == now
            """)
    public ResponseEntity<Collection<ResponseStatsOrders>> getStatsOrders(@ParameterObject RequestFromTo fromTo) {
        var rs = statisticsQuery.getStatsOrders(fromTo);
        return ResponseEntity.ok(rs);
    }

    @GetMapping("trends")
    public ResponseEntity<Collection<ResponseStatsProductTrend>> getTrends(@ParameterObject RequestFromTo fromTo, @RequestParam(required = false, defaultValue = "10") int size) {
        var rs = statisticsQuery.getProductTrends(fromTo, size);
        return ResponseEntity.ok(rs);
    }


}
