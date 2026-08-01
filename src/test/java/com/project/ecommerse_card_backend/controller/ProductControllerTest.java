package com.project.ecommerse_card_backend.controller;

import com.project.ecommerse_card_backend.dto.productdto.ProductResponse;
import com.project.ecommerse_card_backend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    // ==========================================
    // getAllProducts() — Positive
    // ==========================================
    @Test
    void testGetAllProducts_ReturnsList() {
        ProductResponse product = new ProductResponse(1L, "Laptop", "laptop", "desc", new BigDecimal("999.99"), 10, null);
        when(productService.getAllProducts()).thenReturn(List.of(product));

        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts();

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    // ==========================================
    // getAllProducts() — Empty list
    // ==========================================
    @Test
    void testGetAllProducts_ReturnsEmpty() {
        when(productService.getAllProducts()).thenReturn(List.of());

        ResponseEntity<List<ProductResponse>> response = productController.getAllProducts();

        assertNotNull(response);
        assertTrue(response.getBody().isEmpty());
    }
}
