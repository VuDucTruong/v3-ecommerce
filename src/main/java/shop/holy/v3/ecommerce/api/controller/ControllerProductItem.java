package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.item.RequestProductItemCreate;
import shop.holy.v3.ecommerce.api.dto.product.item.RequestProductItemSearch;
import shop.holy.v3.ecommerce.api.dto.product.item.RequestProductItemUpdate;
import shop.holy.v3.ecommerce.api.dto.product.item.ResponseProductItem;
import shop.holy.v3.ecommerce.service.biz.ProductItemService;

@Tag(name = "Product Items")
@RestController
@RequestMapping("/products/items")
@RequiredArgsConstructor
public class ControllerProductItem {
    private final ProductItemService productItemService;

    @PostMapping("/searches")
    public ResponseEntity<ResponsePagination<ResponseProductItem>> search(@RequestBody RequestProductItemSearch searchReq) {
        ResponsePagination<ResponseProductItem> productItems = productItemService.search(searchReq);
        return ResponseEntity.ok(productItems);
    }

    @GetMapping("")
    public ResponseProductItem getProductItemByUniqueIdentifier(
            @RequestParam(required = false, defaultValue = Integer.MIN_VALUE + "") long id,
            @RequestParam(required = false) String productKey,
            @RequestParam(required = false) boolean deleted
    ) {
        return productItemService.getByIdentifier(id, productKey, deleted);
    }


    @PostMapping("")
    public ResponseEntity<ResponseProductItem[]> createProductItem(@RequestBody RequestProductItemCreate[] productItem) {
        if (productItem != null && productItem.length > 0) {
            return null;
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("")
    public ResponseProductItem updateProductItem(@RequestBody RequestProductItemUpdate productItemUpdate) {
        return productItemService.update(productItemUpdate);
    }


}
