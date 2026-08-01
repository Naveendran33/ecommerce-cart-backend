package com.project.ecommerse_card_backend.dto.cartdto;

import java.util.List;

public record CartResponse(
        Long id,
        List<CartItemResponse> items,
        String couponCode
) {
}
