package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseStatistics;
import shop.holy.v3.ecommerce.service.biz.StatisticsService;

import java.util.concurrent.CompletableFuture;

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
        return statisticsService.getStats(backDayTotals, backDayRevenues).thenApply(ResponseEntity::ok);
    }


}
