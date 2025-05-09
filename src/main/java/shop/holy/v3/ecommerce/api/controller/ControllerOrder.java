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

@RequiredArgsConstructor
@RestController
@Tag(name = "Orders")
@RequestMapping("orders")
public class ControllerOrder {
    private final OrderService orderService;
    private final OrderInsertService orderInsertService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable long id,
                                          @RequestParam(required = false) boolean deleted) {
        return ResponseEntity.ok(orderService.findById(id, deleted));
    }


    @PostMapping("/searches")
    @Operation(description = """
            1.automatically filter by Id when: user is not admin \n
            2. concatenated queries with AND condition!!
            """)
    public ResponseEntity<?> search(
            RequestOrderSearch searchReq
    ) {
        ResponsePagination<ResponseOrder> categories = orderService.search(searchReq);
        return ResponseEntity.ok(categories);
    }


    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@RequestBody @Valid RequestOrderCreate request) {
        return ResponseEntity.ok(orderInsertService.insert(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable long id) {
        return ResponseEntity.ok(orderService.deleteOrderById(id));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteMultiples(@RequestParam long[] ids) {
        return ResponseEntity.ok(orderService.deleteOrders(ids));
    }


}
