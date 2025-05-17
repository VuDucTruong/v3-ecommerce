package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.order.RequestOrderCreate;
import shop.holy.v3.ecommerce.api.dto.order.RequestOrderSearch;
import shop.holy.v3.ecommerce.api.dto.order.ResponseOrder;
import shop.holy.v3.ecommerce.service.biz.OrderInsertService;
import shop.holy.v3.ecommerce.service.biz.OrderService;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@Tag(name = "Orders", description = "doesn't support Update order")
@RequestMapping("orders")
public class ControllerOrder {
    private final OrderService orderService;
    private final OrderInsertService orderInsertService;

    @Operation(summary = "get 1")
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ResponseOrder>> getOrderById(@PathVariable long id,
                                          @RequestParam(required = false) boolean deleted) {
        return orderService.findById(id, deleted).thenApply(ResponseEntity::ok);
    }


    @PostMapping("/searches")
    @Operation(description = """
            1.automatically filter by Id when: user is not admin \n
            2. concatenated queries with AND condition!!
            """)
    public ResponseEntity<ResponsePagination<ResponseOrder>> search(
            RequestOrderSearch searchReq
    ) {
        ResponsePagination<ResponseOrder> categories = orderService.search(searchReq);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "create 1")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody @Valid RequestOrderCreate request) {
        return ResponseEntity.ok(orderInsertService.insert(request));
    }

    @Operation(summary = "delete 1")
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteOrderById(@PathVariable long id) {
        return ResponseEntity.ok(orderService.deleteOrderById(id));
    }

    @Operation(summary = "delete many")
    @DeleteMapping("")
    public ResponseEntity<Integer> deleteMultiples(@RequestParam long[] ids) {
        return ResponseEntity.ok(orderService.deleteOrders(ids));
    }


}
