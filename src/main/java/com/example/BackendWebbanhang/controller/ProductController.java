package com.example.BackendWebbanhang.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.BackendWebbanhang.domain.Product;
import com.example.BackendWebbanhang.service.ProductService;
import com.example.BackendWebbanhang.util.annotation.ApiMessage;
import com.example.BackendWebbanhang.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    @ApiMessage("Create a new product")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product newProduct = this.productService.handleCreateProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PutMapping("/products")
    @ApiMessage("Update a product")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) throws IdInvalidException {
        Product updatedProduct = this.productService.handleUpdateProduct(product);
        if (updatedProduct == null) {
            throw new IdInvalidException("Sản phẩm với id = " + product.getId() + " không tồn tại");
        }
        return ResponseEntity.ok(updatedProduct);
    }

    @GetMapping("/products/{id}")
    @ApiMessage("Fetch product by id")
    public ResponseEntity<Product> getProductById(@PathVariable("id") long id) throws IdInvalidException {
        Product product = this.productService.fetchProductById(id);
        if (product == null) {
            throw new IdInvalidException("Sản phẩm không tồn tại");
        }
        return ResponseEntity.ok(product);
    }

    @GetMapping("/products")
    @ApiMessage("Fetch all products with pagination & filter")
    public ResponseEntity<Object> getAllProducts(
            @Filter Specification<Product> spec, Pageable pageable) {
        return ResponseEntity.ok(this.productService.fetchAllProducts(spec, pageable));
    }

    @DeleteMapping("/products/{id}")
    @ApiMessage("Soft delete a product")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") long id) throws IdInvalidException {
        if (this.productService.fetchProductById(id) == null) {
            throw new IdInvalidException("Sản phẩm với id = " + id + " không tồn tại");
        }
        this.productService.handleDeleteProduct(id);
        return ResponseEntity.ok(null);
    }
}