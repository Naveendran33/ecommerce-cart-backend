package com.project.ecommerse_card_backend.dto.cartdto;

import com.project.ecommerse_card_backend.dto.productdto.ProductResponse;

public record CartItemResponse(
        Long id,
        ProductResponse product,
        Integer quantity
) {
}
