package com.project.ecommerse_card_backend.dto.orderdto;

import jakarta.validation.constraints.NotBlank;

public record CheckoutRequest(
        @NotBlank(message = "Shipping address is required")
        String shippingAddressJson,
        
        @NotBlank(message = "Billing address is required")
        String billingAddressJson,
        
        String couponCode
) {
}
