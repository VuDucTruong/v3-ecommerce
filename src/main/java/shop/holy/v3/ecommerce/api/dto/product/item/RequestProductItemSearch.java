package shop.holy.v3.ecommerce.api.dto.product.item;

import shop.holy.v3.ecommerce.api.dto.RequestPageable;

public record RequestProductItemSearch(
        RequestPageable pageRequest,
        String productName,
        Long productId,
        String productKey,
        boolean used,
        boolean deleted
) {
}
