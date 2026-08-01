package com.project.ecommerse_card_backend.dto.orderdto;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        BigDecimal subTotal,
        BigDecimal discountAmount,
        BigDecimal taxAmount,
        BigDecimal totalAmount,
        String couponCode,
        String status,
        String shippingAddressJson,
        String billingAddressJson,
        List<OrderItemResponse> items
) {
}
