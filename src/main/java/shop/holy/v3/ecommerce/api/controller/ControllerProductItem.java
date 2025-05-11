package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponseError;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.item.*;
import shop.holy.v3.ecommerce.service.biz.ProductItemService;
import shop.holy.v3.ecommerce.shared.constant.DefaultValues;
import shop.holy.v3.ecommerce.shared.exception.BadRequestException;

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
    @Operation(description = """
            there're 4 possible parameters, but must be 1 to present only: \n
            1. id -> get product Item by id \n
            2. productId -> get All ProductItems by productId
            3. orderDetailId -> get All ProductItems sent via Email by orderDetailId
            4. productKey -> rarely but ok, to check if exists
            """)
    public ResponseEntity<ResponseProductItems_Indetails[]> getProductItemByUniqueIdentifier(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long orderDetailId,
            @RequestParam(required = false) String productKey) {
        int overlapCount = 0;
        if (id != null) overlapCount++;
        if (productId != null) overlapCount++;
        if (orderDetailId != null) overlapCount++;
        if (productKey != null) overlapCount++;
        if (overlapCount > 1)
            throw new BadRequestException("sth");

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
