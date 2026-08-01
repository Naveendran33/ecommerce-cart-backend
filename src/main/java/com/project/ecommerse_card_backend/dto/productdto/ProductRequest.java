package com.project.ecommerse_card_backend.dto.productdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "Name should be given")
        String name,
        String description,
        @NotNull(message = "price should be given")
        BigDecimal price,
        Integer stockQuantity,
        @NotNull(message = "category id should be given")
        Long categoryId
) {
}
