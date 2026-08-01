package com.project.ecommerse_card_backend.service;

import com.project.ecommerse_card_backend.Mapper.ProductMapper;
import com.project.ecommerse_card_backend.dto.productdto.ProductRequest;
import com.project.ecommerse_card_backend.dto.productdto.ProductResponse;
import com.project.ecommerse_card_backend.entity.Category;
import com.project.ecommerse_card_backend.entity.Product;
import com.project.ecommerse_card_backend.exception.ResourceNotFoundException;
import com.project.ecommerse_card_backend.repository.CategoryRepository;
import com.project.ecommerse_card_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponse createProduct(ProductRequest request){
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category Not found"));

        Product product = new Product();
        product.setCategory(category);
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setSlug(request.name().toLowerCase().replaceAll("\\s+","-"));

        Product savedProduct = productRepository.save(product);

        return ProductMapper.productToProductResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::productToProductResponse)
                .toList();
    }

    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(ProductMapper::productToProductResponse)
                .toList();
    }
}
