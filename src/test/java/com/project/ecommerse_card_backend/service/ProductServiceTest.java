package com.project.ecommerse_card_backend.service;

import com.project.ecommerse_card_backend.dto.productdto.ProductResponse;
import com.project.ecommerse_card_backend.entity.Category;
import com.project.ecommerse_card_backend.entity.Product;
import com.project.ecommerse_card_backend.exception.ResourceNotFoundException;
import com.project.ecommerse_card_backend.repository.CategoryRepository;
import com.project.ecommerse_card_backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");
        testCategory.setSlug("electronics");

        testProduct = new Product();
        testProduct.setId(100L);
        testProduct.setName("Laptop");
        testProduct.setSlug("laptop");
        testProduct.setPrice(new BigDecimal("999.99"));
        testProduct.setStockQuantity(10);
        testProduct.setCategory(testCategory);
    }

    // ==========================================
    // getAllProducts() — Positive
    // ==========================================
    @Test
    void testGetAllProducts_ReturnsList() {
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        List<ProductResponse> responses = productService.getAllProducts();

        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals("Laptop", responses.get(0).name());
    }

    // ==========================================
    // getAllProducts() — Returns empty list
    // ==========================================
    @Test
    void testGetAllProducts_ReturnsEmpty() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<ProductResponse> responses = productService.getAllProducts();

        assertTrue(responses.isEmpty());
    }

    // ==========================================
    // getProductsByCategory() — Positive
    // ==========================================
    @Test
    void testGetProductsByCategory_Success() {
        when(productRepository.findByCategoryId(1L)).thenReturn(List.of(testProduct));

        List<ProductResponse> responses = productService.getProductsByCategory(1L);

        assertFalse(responses.isEmpty());
        assertEquals("Laptop", responses.get(0).name());
    }

    // ==========================================
    // getProductsByCategory() — Negative: No products
    // ==========================================
    @Test
    void testGetProductsByCategory_Empty() {
        when(productRepository.findByCategoryId(99L)).thenReturn(List.of());

        List<ProductResponse> responses = productService.getProductsByCategory(99L);

        assertTrue(responses.isEmpty());
    }

    // ==========================================
    // createProduct() — Positive
    // ==========================================
    @Test
    void testCreateProduct_Success() {
        var request = new com.project.ecommerse_card_backend.dto.productdto.ProductRequest(
                "Laptop", "A fast laptop", new BigDecimal("999.99"), 10, 1L
        );

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        ProductResponse response = productService.createProduct(request);

        assertNotNull(response);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    // ==========================================
    // createProduct() — Negative: Category not found
    // ==========================================
    @Test
    void testCreateProduct_CategoryNotFound() {
        var request = new com.project.ecommerse_card_backend.dto.productdto.ProductRequest(
                "Laptop", "A fast laptop", new BigDecimal("999.99"), 10, 99L
        );

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.createProduct(request);
        });
    }
}
