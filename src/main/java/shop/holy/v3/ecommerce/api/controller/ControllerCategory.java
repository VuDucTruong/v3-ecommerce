package shop.holy.v3.ecommerce.api.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategoryCreate;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategorySearch;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategoryUpdate;
import shop.holy.v3.ecommerce.api.dto.category.ResponseCategory;
import shop.holy.v3.ecommerce.service.biz.category.CategoryService;

@Tag(name = "Categories")
@RestController
@RequiredArgsConstructor
@RequestMapping("categories")
public class ControllerCategory {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    @Operation(summary = "get 1")
    public ResponseEntity<ResponseCategory> getOne(@PathVariable long id,
                                                   @RequestParam(required = false) boolean deleted) {
        return ResponseEntity.ok(categoryService.getCategoryByCode(id, deleted));
    }

    @PostMapping("/searches")
    public ResponseEntity<ResponsePagination<ResponseCategory>> search(
            @RequestBody RequestCategorySearch searchReq) {
        ResponsePagination<ResponseCategory> categories = categoryService.search(searchReq);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "create 1")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<ResponseCategory> createCategory(@ModelAttribute RequestCategoryCreate request) {
        ResponseCategory res = categoryService.insert(request);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "update 1")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<ResponseCategory> updateCategory(@ModelAttribute RequestCategoryUpdate request) {
        ResponseCategory res = categoryService.update(request);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "delete 1")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<Integer> deleteCategory(@PathVariable long id,
                                                  @RequestParam(required = false) boolean isHard) {
        var changes = categoryService.deleteCategory(id, isHard);
        return ResponseEntity.ok(changes);
    }


    @DeleteMapping("")
    @Operation(summary = "delete many")
    public ResponseEntity<Integer> deleteCategories(@RequestParam long[] ids, @RequestParam(required = false) boolean isHard) {
        var changes = categoryService.deleteCategories(ids, isHard);
        return ResponseEntity.ok(changes);
    }

}