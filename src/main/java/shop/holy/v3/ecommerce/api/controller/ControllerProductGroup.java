package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.product.group.RequestProductGroupCreate;
import shop.holy.v3.ecommerce.api.dto.product.group.RequestProductGroupUpdate;
import shop.holy.v3.ecommerce.api.dto.product.group.ResponseProductGroup;
import shop.holy.v3.ecommerce.service.biz.product.group.ProductGroupCommand;

@RequiredArgsConstructor
@RestController
@Tag(name = "Product Groups", description = "đã censored, only use GET Many here")
@RequestMapping("products/groups")
public class ControllerProductGroup {
    private final ProductGroupCommand productGroupCommand;

    @Operation(summary = "get many")
    @GetMapping("")
    public ResponseEntity<ResponseProductGroup[]> getAllProductGroups() {
        ResponseProductGroup[] rss = productGroupCommand.getAllProductGroups();
        return rss == null ? ResponseEntity.ok().build() : ResponseEntity.ok(rss);
    }

    @Operation(summary = "get 1")
    @GetMapping("{id}")
    public ResponseEntity<ResponseProductGroup> getById(@PathVariable long id,
                                                        @RequestParam(required = false) boolean deleted) {
        ResponseProductGroup group = productGroupCommand.getProductGroupById(id, deleted);
        return ResponseEntity.ok(group);
    }

    @Operation(hidden = true)
    @PostMapping("")
    public ResponseEntity<ResponseProductGroup> insert(@RequestBody RequestProductGroupCreate request) {
        ResponseProductGroup group = productGroupCommand.insertProductGroup(request);
        return ResponseEntity.ok(group);
    }


    @Operation(hidden = true)
    @PutMapping("")
    public ResponseEntity<ResponseProductGroup> update(@RequestBody RequestProductGroupUpdate request) {
        ResponseProductGroup group = productGroupCommand.updateProductGroup(request);
        return ResponseEntity.ok(group);
    }

    @Operation(hidden = true)
    @DeleteMapping("")
    public ResponseEntity<?> delete(@PathVariable long id) {
        productGroupCommand.deleteProductGroup(id);
        return ResponseEntity.ok().build();
    }


}
