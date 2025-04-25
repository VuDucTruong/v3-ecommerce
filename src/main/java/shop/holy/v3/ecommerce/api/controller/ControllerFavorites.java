package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.service.biz.ProductService;


@RequiredArgsConstructor
@RestController
@Tag(name = "Products Favorites")
@RequestMapping("products/favorites")
public class ControllerFavorites {
    private final ProductService productService;

    @PutMapping
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<?> favoriteProduct(@RequestParam Long productId) {
        productService.addFavorite(productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<?> unFavoriteProduct(@RequestParam Long productId) {
        productService.removeFavorite(productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<?> getFavoriteProducts(@ParameterObject Pageable pageable) {
        ResponsePagination<ResponseProduct> res = productService.findFavorites(pageable);
        return ResponseEntity.ok(res);
    }

}