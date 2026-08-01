package com.project.ecommerse_card_backend.Mapper;

import com.project.ecommerse_card_backend.dto.productdto.ProductResponse;
import com.project.ecommerse_card_backend.entity.Product;

public class ProductMapper {

    public static ProductResponse productToProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                CategoryMapper.categoryToCategoryResponse(product.getCategory())
        );
    }
}
