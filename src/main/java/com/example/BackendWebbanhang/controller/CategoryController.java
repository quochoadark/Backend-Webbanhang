package com.example.BackendWebbanhang.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.BackendWebbanhang.domain.Category;
import com.example.BackendWebbanhang.service.CategoryService;
import com.example.BackendWebbanhang.util.annotation.ApiMessage;
import com.example.BackendWebbanhang.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    @ApiMessage("Create a new category")
    public ResponseEntity<Category> createNewCategory(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.categoryService.handleCreateCategory(category));
    }

    @PutMapping("/categories")
    @ApiMessage("Update a category")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) throws IdInvalidException {
        Category updatedCategory = this.categoryService.handleUpdateCategory(category);
        if (updatedCategory == null) {
            throw new IdInvalidException("Category với id = " + category.getId() + " không tồn tại");
        }
        return ResponseEntity.ok(updatedCategory);
    }

    @GetMapping("/categories/{id}")
    @ApiMessage("Fetch category by id")
    public ResponseEntity<Category> getCategory(@PathVariable("id") long id) {
        return ResponseEntity.ok(this.categoryService.fetchCategoryById(id));
    }

    @GetMapping("/categories")
    @ApiMessage("Fetch all categories with pagination")
    public ResponseEntity<Object> getAllCategories(
            @Filter Specification<Category> spec, Pageable pageable) {
        return ResponseEntity.ok(this.categoryService.fetchAllCategories(spec, pageable));
    }

    @DeleteMapping("/categories/{id}")
    @ApiMessage("Soft delete a category")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") long id) throws IdInvalidException {
        if (this.categoryService.fetchCategoryById(id) == null) {
            throw new IdInvalidException("Category với id = " + id + " không tồn tại");
        }
        this.categoryService.handleDeleteCategory(id);
        return ResponseEntity.ok(null);
    }
}