package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.item.*;
import shop.holy.v3.ecommerce.service.biz.ProductItemService;
import shop.holy.v3.ecommerce.shared.constant.DefaultValues;

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
            @RequestParam(required = false, defaultValue = DefaultValues.ID + "") long id,
            @RequestParam(required = false) String productKey,
            @RequestParam(required = false) boolean deleted
    ) {

        return productItemService.getByIdentifier(id, productKey, deleted);
    }


    @PostMapping("")
    public ResponseEntity<ResponseProductItemCreate> createProductItem(@RequestBody @Valid RequestProductItemCreate[] productItem) {
        if (productItem == null || productItem.length == 0) {
            return ResponseEntity.ok().build();
        }
        ResponseProductItemCreate res = productItemService.inserts(productItem);
        return ResponseEntity.ok(res);
    }

    @PutMapping("")
    public ResponseProductItem updateProductItem(@RequestBody RequestProductItemUpdate productItemUpdate) {
        return productItemService.update(productItemUpdate);
    }

    @DeleteMapping("")
    public ResponseEntity<Integer> deleteProductItems(long[] ids) {
        int changes = productItemService.deleteProductItems(ids);
        return ResponseEntity.ok(changes);
    }


}
