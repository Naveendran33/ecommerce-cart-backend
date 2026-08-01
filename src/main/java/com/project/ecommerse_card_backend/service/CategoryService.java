package com.project.ecommerse_card_backend.service;

import com.project.ecommerse_card_backend.Mapper.CategoryMapper;
import com.project.ecommerse_card_backend.dto.categorydto.CategoryRequest;
import com.project.ecommerse_card_backend.dto.categorydto.CategoryResponse;
import com.project.ecommerse_card_backend.entity.Category;
import com.project.ecommerse_card_backend.exception.ResourceNotFoundException;
import com.project.ecommerse_card_backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(CategoryRequest request){
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        String slug = request.name().toLowerCase().replaceAll("\\s+","-");
        category.setSlug(slug);

        categoryRepository.save(category);

        return CategoryMapper.categoryToCategoryResponse(category);
    }

    public List<CategoryResponse> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMapper::categoryToCategoryResponse).toList();
    }

    @Transactional
    public void deleteCategory(Long id){
        if(categoryRepository.existsById(id)){
            categoryRepository.deleteById(id);
        }else{
            throw new ResourceNotFoundException("Category Not Found");
        }
    }


}
