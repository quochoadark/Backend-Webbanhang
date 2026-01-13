package com.example.BackendWebbanhang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.example.BackendWebbanhang.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    // Tìm kiếm Role theo tên (Dùng để gán role khi Login/Register)
    Role findByName(String name);

    // Kiểm tra tên Role đã tồn tại chưa (Dùng khi tạo mới Role để tránh trùng lặp)
    boolean existsByName(String name);
}