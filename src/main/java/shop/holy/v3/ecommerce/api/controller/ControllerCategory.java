package shop.holy.v3.ecommerce.api.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategoryCreate;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategorySearch;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategoryUpdate;
import shop.holy.v3.ecommerce.api.dto.category.ResponseCategory;
import shop.holy.v3.ecommerce.service.biz.CategoryService;

import java.io.IOException;

@Tag(name = "Categories")
@RestController
@RequiredArgsConstructor
@RequestMapping("accounts")
public class ControllerCategory {

    private final CategoryService categoryService;


    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable long id,
                                    @RequestParam(required = false) boolean deleted) {
        return ResponseEntity.ok(categoryService.getCategoryByCode(id, false));
    }

    @PostMapping("/searches")
    public ResponseEntity<ResponsePagination<ResponseCategory>> search(
            @RequestBody RequestCategorySearch searchReq) {
        ResponsePagination<ResponseCategory> categories = categoryService.search(searchReq);
        return ResponseEntity.ok(categories);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseCategory> createCategory(@ModelAttribute RequestCategoryCreate request) throws IOException {
        ResponseCategory res = categoryService.insert(request);
        return ResponseEntity.ok(res);
    }


    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseCategory> updateCategory(@ModelAttribute RequestCategoryUpdate request) throws IOException {
        ResponseCategory res = categoryService.update(request);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long id) {
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

}