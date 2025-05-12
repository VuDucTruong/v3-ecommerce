package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.service.biz.ProductFavoriteService;


@RequiredArgsConstructor
@RestController
@Tag(name = "Products Favorites")
@RequestMapping("products/favorites")
public class ControllerFavorites {
    private final ProductFavoriteService favoriteService;


    @Operation(summary = "press like")
    @PutMapping
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<?> favoriteProduct(@RequestParam Long productId) {
        favoriteService.addFavorite(productId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "press unlike")
    @DeleteMapping
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<?> unFavoriteProduct(@RequestParam Long productId) {
        favoriteService.removeFavorite(productId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "get My favorite products")
    @GetMapping
    @PreAuthorize("hasRole(T(shop.holy.v3.ecommerce.shared.constant.RoleEnum.Roles).ROLE_LEVEL_0)")
    public ResponseEntity<?> getFavoriteProducts(@ParameterObject Pageable pageable) {
        ResponsePagination<ResponseProduct> res = favoriteService.findFavorites(pageable);
        return ResponseEntity.ok(res);
    }

}