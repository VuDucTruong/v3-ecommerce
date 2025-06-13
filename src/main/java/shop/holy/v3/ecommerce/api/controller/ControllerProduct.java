package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.*;
import shop.holy.v3.ecommerce.api.dto.statistic.ResponseTopProductSold;
import shop.holy.v3.ecommerce.service.biz.StatisticsQuery;
import shop.holy.v3.ecommerce.service.biz.product.ProductCommand;
import shop.holy.v3.ecommerce.service.biz.product.ProductQuery;
import shop.holy.v3.ecommerce.service.biz.product.tag.ProductTagQuery;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@Tag(name = "Products")
@RequestMapping("products")
public class ControllerProduct {

    private final ProductCommand productCommand;
    private final ProductQuery productQuery;
    private final ProductTagQuery tagQuery;

    @Operation
    @PostMapping("/searches")
    public ResponseEntity<ResponsePagination<ResponseProduct>> getAllProducts(@RequestBody RequestProductSearch searchReq) {
        ResponsePagination<ResponseProduct> res = productQuery.search(searchReq);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "get 1", description = """
            Chỉ dùng id hoặc slug \n
            ===> nếu có cả 2 sẽ get exact by id \n
            Với slug: Get by slug == slug (không phải phép like) \n
            Với id: Get by id == id
            """)
    @GetMapping("")
    public CompletableFuture<ResponseProduct> getProductById(@RequestParam(required = false) Long id,
                                                             @RequestParam(required = false) String slug,
                                                             @RequestParam(required = false) boolean deleted) {
        return productQuery.getByIdentifier(id, slug, deleted);
    }

    @Operation(summary = "create 1")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseProduct> createProduct(@ModelAttribute RequestProductCreate request) {
        return ResponseEntity.ok(productCommand.insert(request));
    }


    @Operation(summary = "delete 1")
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteProductById(@PathVariable long id) {
        return ResponseEntity.ok(productCommand.deleteProductById(id));
    }

    @Operation(summary = "delete many")
    @DeleteMapping("")
    public ResponseEntity<Integer> delete(@RequestParam long[] ids) {
        var rs = productCommand.deleteProductByIdIn(ids);
        return ResponseEntity.ok(rs);
    }


    @Operation(summary = "update 1")
    @PutMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseProduct> updateProduct(@ModelAttribute RequestProductUpdate request) throws IOException {
        return ResponseEntity.ok(productCommand.update(request));
    }


    @GetMapping("trends")
    public ResponseEntity<List<ResponseTopProductSold>> get(@RequestParam(required = false) Integer size) {
        var rs = productQuery.getProductsTrend(size);
        return ResponseEntity.ok(rs);
    }

    @GetMapping("tags")
    @Tag(name = "Tags")
    public ResponseEntity<List<String>> getProductTags() {
        var rs = tagQuery.getAll();
        return ResponseEntity.ok(rs);
    }

}
