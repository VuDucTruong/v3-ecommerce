package shop.holy.v3.ecommerce.api.controller;


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
import shop.holy.v3.ecommerce.service.biz.CategoryService;

@Tag(name = "Categories")
@RestController
@RequiredArgsConstructor
@RequestMapping("categories")
public class ControllerCategory {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable long id,
                                    @RequestParam(required = false) boolean deleted) {
        return ResponseEntity.ok(categoryService.getCategoryByCode(id, deleted));
    }

    @PostMapping("/searches")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<ResponsePagination<ResponseCategory>> search(
            @RequestBody RequestCategorySearch searchReq) {
        ResponsePagination<ResponseCategory> categories = categoryService.search(searchReq);
        return ResponseEntity.ok(categories);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<ResponseCategory> createCategory(@ModelAttribute RequestCategoryCreate request) {
        ResponseCategory res = categoryService.insert(request);
        return ResponseEntity.ok(res);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<ResponseCategory> updateCategory(@ModelAttribute RequestCategoryUpdate request) {
        ResponseCategory res = categoryService.update(request);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_ADMIN)")
    public ResponseEntity<?> deleteCategory(@PathVariable long id,
                                            @RequestParam(required = false) boolean isHard) {
        var changes = categoryService.deleteCategory(id, isHard);
        return ResponseEntity.ok(changes);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteCategories(@RequestParam long[] ids, @RequestParam(required = false) boolean isHard) {
        var changes = categoryService.deleteCategories(ids, isHard);
        return ResponseEntity.ok(changes);
    }

}