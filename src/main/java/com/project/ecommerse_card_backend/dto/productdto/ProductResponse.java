package com.project.ecommerse_card_backend.dto.productdto;

import com.project.ecommerse_card_backend.dto.categorydto.CategoryResponse;

import java.math.BigDecimal;

public record ProductResponse(
        Long id, String name, String slug, String description, BigDecimal price, Integer stockQuantity, CategoryResponse category
        ) {
}
