package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.item.*;
import shop.holy.v3.ecommerce.service.biz.ProductItemService;
import shop.holy.v3.ecommerce.shared.exception.BadRequestException;

import java.util.List;

@Tag(name = "Product Items")
@RestController
@RequestMapping("/products/items")
@RequiredArgsConstructor
public class ControllerProductItem {
    private final ProductItemService productItemService;

    @PostMapping("/searches")
    @Operation(description = """
            1. productName -> like %x% \n
            2. productId -> id = 'x' \n
            3. productKey -> like %x% \n
            4. used -> will search the used only \n
            ====> IS Different from <b> deleted </b> ---> only find productKey on "used Table" \n
            """)
    public ResponseEntity<ResponsePagination<ResponseProductItems_Indetails>> search(@RequestBody RequestProductItemSearch searchReq) {
        ResponsePagination<ResponseProductItems_Indetails> productItems = productItemService.search(searchReq);
        return ResponseEntity.ok(productItems);
    }

    @GetMapping("")
    @Operation(summary = "get by identifier as query parameters", description = """
            there're 4 possible parameters, but must be 1 to present only: \n
            1. id -> get product Item by id \n
            2. productId -> get All ProductItems by productId \n
            3. orderDetailId -> get All ProductItems sent via Email by orderDetailId \n
            4. productKey -> find First by productKey = 'exact_value' \n
            5. used -> default false -> if true -> includes even used ones.
            """)
    public ResponseEntity<ResponseProductItems_Indetails[]> getProductItemByUniqueIdentifier(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long orderDetailId,
            @RequestParam(required = false) String productKey,
            @RequestParam(required = false) boolean used) {
        int overlapCount = 0;
        if (id != null) overlapCount++;
        if (productId != null) overlapCount++;
        if (orderDetailId != null) overlapCount++;
        if (productKey != null) overlapCount++;
        if (overlapCount > 1)
            throw new BadRequestException("sth");

        return ResponseEntity.ok(productItemService.getByIdentifier(id, productId, orderDetailId, productKey, used));
    }


    @Operation(summary = "create 1", description = "")
    @PostMapping("")
    public ResponseEntity<ResponseProductItemCreate> createProductItem(
            @RequestBody @Valid @Nonnull @NotEmpty(message = "Request for creation must not be empty") List< @Valid RequestProductItemCreate> productItem,
            @RequestParam(required = false) boolean used,
            @RequestParam(required = false) boolean ignoreDeleted
    ) {

        ResponseProductItemCreate res = productItemService.inserts(productItem, used, ignoreDeleted);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "update 1")
    @PutMapping("")
    public int updateProductItem(@RequestBody RequestProductItemUpdate productItemUpdate) {
        return productItemService.update(productItemUpdate);
    }

    @Operation(summary = "delete 1")
    @DeleteMapping("")
    public ResponseEntity<Integer> deleteProductItems(@RequestParam long[] ids) {
        int changes = productItemService.deleteProductItems(ids);
        return ResponseEntity.ok(changes);
    }

    @PutMapping("/used")
    @Operation(summary = "mark used => delete at productItems => insert to used table")
    public ResponseEntity<Integer> markUsed(@RequestParam long[] ids) {
        if (ids == null || ids.length == 0) {
            return ResponseEntity.ok().build();
        }
        int changes = productItemService.makeProductUsed(ids);
        return ResponseEntity.ok(changes);
    }


}
