package com.example.BackendWebbanhang.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.BackendWebbanhang.domain.Product;
import com.example.BackendWebbanhang.domain.Category;
import com.example.BackendWebbanhang.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public Product handleCreateProduct(Product product) {
        if (product.getCategory() != null && product.getCategory().getId() > 0) {
            Category cat = this.categoryService.fetchCategoryById(product.getCategory().getId());
            product.setCategory(cat != null ? cat : null);
        }
        return this.productRepository.save(product);
    }

    public Product fetchProductById(long id) {
        Optional<Product> productOptional = this.productRepository.findById(id);
        return productOptional.orElse(null);
    }

    public Page<Product> fetchAllProducts(Specification<Product> spec, Pageable pageable) {
        return this.productRepository.findAll(spec, pageable);
    }

    public Product handleUpdateProduct(Product reqProduct) {
        Product currentProduct = this.fetchProductById(reqProduct.getId());
        if (currentProduct != null) {
            // Cập nhật các thông tin cơ bản
            currentProduct.setName(reqProduct.getName());
            currentProduct.setPrice(reqProduct.getPrice());
            currentProduct.setQuantity(reqProduct.getQuantity());
            currentProduct.setDescription(reqProduct.getDescription());
            currentProduct.setImage(reqProduct.getImage()); // Cập nhật tên file ảnh mới nếu có

            // Cập nhật Category mới nếu Admin thay đổi
            if (reqProduct.getCategory() != null && reqProduct.getCategory().getId() > 0) {
                Category cat = this.categoryService.fetchCategoryById(reqProduct.getCategory().getId());
                currentProduct.setCategory(cat != null ? cat : null);
            }
            return this.productRepository.save(currentProduct);
        }
        return null;
    }

    public void handleDeleteProduct(long id) {
        Product currentProduct = this.fetchProductById(id);
        if (currentProduct != null) {
            // SOFT DELETE: Chuyển active về false để không làm hỏng dữ liệu OrderDetail
            currentProduct.setActive(false);
            this.productRepository.save(currentProduct);
        }
    }
}