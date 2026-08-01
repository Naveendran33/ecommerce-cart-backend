package com.project.ecommerse_card_backend.dto.orderdto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        String productName,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal lineTotal
) {
}
