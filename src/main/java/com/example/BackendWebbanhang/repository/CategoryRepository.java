package com.example.BackendWebbanhang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.example.BackendWebbanhang.domain.Category;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    // Tìm các danh mục chưa bị xóa mềm
    List<Category> findByActiveTrue();
}