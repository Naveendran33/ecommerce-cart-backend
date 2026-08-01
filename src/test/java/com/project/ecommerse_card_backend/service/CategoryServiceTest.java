package com.project.ecommerse_card_backend.service;

import com.project.ecommerse_card_backend.dto.categorydto.CategoryRequest;
import com.project.ecommerse_card_backend.dto.categorydto.CategoryResponse;
import com.project.ecommerse_card_backend.entity.Category;
import com.project.ecommerse_card_backend.exception.ResourceNotFoundException;
import com.project.ecommerse_card_backend.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    // ==========================================
    // getAllCategories() — Positive
    // ==========================================
    @Test
    void testGetAllCategories_ReturnsList() {
        Category cat = new Category();
        cat.setName("Electronics");
        when(categoryRepository.findAll()).thenReturn(List.of(cat));

        List<CategoryResponse> responses = categoryService.getAllCategories();

        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
    }

    // ==========================================
    // getAllCategories() — Returns empty list
    // ==========================================
    @Test
    void testGetAllCategories_ReturnsEmpty() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        List<CategoryResponse> responses = categoryService.getAllCategories();

        assertTrue(responses.isEmpty());
    }

    // ==========================================
    // createCategory() — Positive
    // ==========================================
    @Test
    void testCreateCategory_Success() {
        CategoryRequest request = new CategoryRequest("Electronics", "All electronics");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Electronics");
        savedCategory.setSlug("electronics");

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryResponse response = categoryService.createCategory(request);

        assertNotNull(response);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    // ==========================================
    // deleteCategory() — Positive
    // ==========================================
    @Test
    void testDeleteCategory_Success() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(1L);

        assertDoesNotThrow(() -> categoryService.deleteCategory(1L));

        verify(categoryRepository, times(1)).deleteById(1L);
    }

    // ==========================================
    // deleteCategory() — Negative: Not found
    // ==========================================
    @Test
    void testDeleteCategory_NotFound() {
        when(categoryRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.deleteCategory(99L);
        });

        verify(categoryRepository, never()).deleteById(any());
    }
}
