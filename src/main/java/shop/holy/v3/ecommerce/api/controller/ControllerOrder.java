package shop.holy.v3.ecommerce.api.controller;

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
import shop.holy.v3.ecommerce.service.biz.OrderService;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Tag(name = "Orders")
@RequestMapping("orders")
public class ControllerOrder {
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable long id) {
        return ResponseEntity.ok(orderService.findByCode(id));
    }


    @PostMapping("/searches")
    public ResponseEntity<?> search(
            RequestOrderSearch searchReq
    ) {
        ResponsePagination<ResponseOrder> categories = orderService.search(searchReq);
        return ResponseEntity.ok(categories);
    }


    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@RequestBody @Valid RequestOrderCreate request) {
        return ResponseEntity.ok(orderService.insert(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable long id ) {
        return ResponseEntity.ok(orderService.deleteOrderById(id));
    }



}
