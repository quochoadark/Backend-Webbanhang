package com.example.BackendWebbanhang.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.BackendWebbanhang.domain.Category;
import com.example.BackendWebbanhang.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category handleCreateCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    public Category fetchCategoryById(long id) {
        Optional<Category> categoryOptional = this.categoryRepository.findById(id);
        return categoryOptional.orElse(null);
    }

    public Page<Category> fetchAllCategories(Specification<Category> spec, Pageable pageable) {
        return this.categoryRepository.findAll(spec, pageable);
    }

    public Category handleUpdateCategory(Category reqCategory) {
        Category currentCategory = this.fetchCategoryById(reqCategory.getId());
        if (currentCategory != null) {
            currentCategory.setName(reqCategory.getName());
            currentCategory.setDescription(reqCategory.getDescription());
            currentCategory = this.categoryRepository.save(currentCategory);
        }
        return currentCategory;
    }

    public void handleDeleteCategory(long id) {
        Category currentCategory = this.fetchCategoryById(id);
        if (currentCategory != null) {
            // SOFT DELETE: Không xóa khỏi DB, chỉ set active = false
            currentCategory.setActive(false);
            this.categoryRepository.save(currentCategory);
        }
    }
}