package com.project.ecommerse_card_backend.dto.categorydto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "Name should be given")
        String name,
        @NotBlank(message = "Description should be given")
        String description
) {
}
