package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductCreate;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductSearch;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductUpdate;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.service.biz.ProductService;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Tag(name = "Products")
@RequestMapping("products")
public class ControllerProduct {

    private final ProductService productService;

    @PostMapping("/searches")
    public ResponseEntity<?> getAllProducts(@RequestBody RequestProductSearch searchReq) {
        ResponsePagination<ResponseProduct> res = productService.search(searchReq);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable int id,
                                            @RequestParam(required = false) boolean deleted) {
        ResponseProduct res = productService.findById(id, deleted);
        return ResponseEntity.ok(res);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(@ModelAttribute RequestProductCreate request) throws IOException {
        return ResponseEntity.ok(productService.insert(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable long id) {
        return ResponseEntity.ok(productService.deleteProductById(id));
    }

    @PutMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(@ModelAttribute RequestProductUpdate request) throws IOException {
        return ResponseEntity.ok(productService.update(request));
    }

}
