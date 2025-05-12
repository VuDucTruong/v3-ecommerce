package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@Tag(name = "Products")
@RequestMapping("products")
public class ControllerProduct {

    private final ProductService productService;

    @Operation
    @PostMapping("/searches")
    public ResponseEntity<?> getAllProducts(@RequestBody RequestProductSearch searchReq) {
        ResponsePagination<ResponseProduct> res = productService.search(searchReq);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "get 1")
    @GetMapping("")
    public CompletableFuture<ResponseProduct> getProductById(@RequestParam(required = false) Long id,
                                                             @RequestParam(required = false) String slug,
                                                             @RequestParam(required = false) boolean deleted) {
        return productService.getByIdentifier(id, slug, deleted);
    }

    @Operation(summary = "create 1")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(@ModelAttribute RequestProductCreate request) {
        return ResponseEntity.ok(productService.insert(request));
    }


    @Operation(summary = "delete 1")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable long id) {
        return ResponseEntity.ok(productService.deleteProductById(id));
    }

    @Operation(summary = "delete many")
    @DeleteMapping("")
    public ResponseEntity<?> delete(@RequestParam long[] ids) {
        var rs = productService.deleteProductByIdIn(ids);
        return ResponseEntity.ok(rs);
    }


    @Operation(summary = "update 1")
    @PutMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(@ModelAttribute RequestProductUpdate request) throws IOException {
        return ResponseEntity.ok(productService.update(request));
    }

}
