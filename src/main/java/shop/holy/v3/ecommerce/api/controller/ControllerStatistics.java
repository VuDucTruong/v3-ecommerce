package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatTotal;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatistics;
import shop.holy.v3.ecommerce.service.biz.StatisticsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@RestController
@Tag(name = "Statistics", description = "=> for users home page")
@RequiredArgsConstructor
@RequestMapping("statistics")
public class ControllerStatistics {
    private final StatisticsService statisticsService;

    @GetMapping("")
    public CompletableFuture<ResponseEntity<ResponseStatistics>> get(
            @RequestParam(required = false, defaultValue = "7") int backDayTotals,
            @RequestParam(required = false, defaultValue = "7") int backDayRevenues
    ) {
        Random random = new Random();
        CompletableFuture<ResponseStatistics> temp = CompletableFuture.supplyAsync(() -> {
            ResponseStatTotal totals = new ResponseStatTotal(backDayTotals * 10, backDayTotals, BigDecimal.valueOf(backDayTotals * 1_000_000L),
                    backDayTotals);
            LocalDate now = LocalDate.now();
            var revenues =  IntStream.iterate(backDayTotals, i -> i >= 0, i -> i - 1)
                    .mapToObj(i -> new ResponseStatistics.ResponseStatRevenues(now.minusDays(i), BigDecimal.valueOf(random.nextInt(backDayRevenues) * 1_000_000L)))
                    .toList();
            return new ResponseStatistics(totals, revenues);
        });
        if(true)
            return temp.thenApply(ResponseEntity::ok);

        return statisticsService.getStats(backDayTotals, backDayRevenues).thenApply(ResponseEntity::ok);
    }


}
