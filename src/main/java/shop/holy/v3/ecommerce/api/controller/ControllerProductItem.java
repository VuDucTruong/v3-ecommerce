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
    public ResponseEntity<ResponseProductItem[]> getProductItemByUniqueIdentifier(
            @RequestParam(required = false, defaultValue = DefaultValues.ID + "") long id,
            @RequestParam(required = false, defaultValue = DefaultValues.ID + "") long productId,
            @RequestParam(required = false, defaultValue = DefaultValues.ID + "") long orderDetailId,
            @RequestParam(required = false) String productKey) {
        int overlapCount = 0;
        if (id != DefaultValues.ID) overlapCount++;
        if (productId != DefaultValues.ID) overlapCount++;
        if (orderDetailId != DefaultValues.ID) overlapCount++;
        if (productKey != null) overlapCount++;
        if (overlapCount > 1)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(productItemService.getByIdentifier(id, productId, orderDetailId, productKey));
    }


    @PostMapping("")
    public ResponseEntity<ResponseProductItemCreate> createProductItem(
            @RequestBody @Valid RequestProductItemCreate[] productItem,
            @RequestParam(required = false) boolean used) {
        if (productItem == null || productItem.length == 0) {
            return ResponseEntity.ok().build();
        }
        ResponseProductItemCreate res = productItemService.inserts(productItem, used);
        return ResponseEntity.ok(res);
    }

    @PutMapping("")
    public int updateProductItem(@RequestBody RequestProductItemUpdate productItemUpdate) {
        return productItemService.update(productItemUpdate);
    }

    @DeleteMapping("")
    public ResponseEntity<Integer> deleteProductItems(@RequestParam long[] ids) {
        int changes = productItemService.deleteProductItems(ids);
        return ResponseEntity.ok(changes);
    }

    @PutMapping("/mark-used")
    public ResponseEntity<Integer> markUsed(@RequestParam long[] ids) {
        if (ids == null || ids.length == 0) {
            return ResponseEntity.ok().build();
        }
        int changes = productItemService.makeProductUsed(ids);
        return ResponseEntity.ok(changes);
    }


}
