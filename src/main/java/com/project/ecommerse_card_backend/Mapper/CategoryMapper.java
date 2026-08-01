package com.project.ecommerse_card_backend.Mapper;


import com.project.ecommerse_card_backend.dto.categorydto.CategoryResponse;
import com.project.ecommerse_card_backend.entity.Category;

public class CategoryMapper {

    public static CategoryResponse categoryToCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getDescription(),category.getSlug());
    }

}

