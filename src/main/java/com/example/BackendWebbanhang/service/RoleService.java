package com.example.BackendWebbanhang.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.BackendWebbanhang.domain.Role;
import com.example.BackendWebbanhang.domain.response.user.ResultPaginationDTO;
import com.example.BackendWebbanhang.repository.RoleRepository;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public boolean existsByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role create(Role role) {
        return this.roleRepository.save(role);
    }

    public Role fetchById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        return roleOptional.isPresent() ? roleOptional.get() : null;
    }

    public Role fetchByName(String name) {
        return this.roleRepository.findByName(name);
    }

    public Role update(Role r) {
        Role roleDB = this.fetchById(r.getId());
        if (roleDB != null) {
            roleDB.setName(r.getName());
            roleDB.setDescription(r.getDescription());
            roleDB.setActive(r.isActive());
            // Sau này nếu có thêm Permissions thì update ở đây
            return this.roleRepository.save(roleDB);
        }
        return null;
    }

    public void delete(long id) {
        this.roleRepository.deleteById(id);
    }

    // Hàm lấy danh sách kèm phân trang (Dùng cho trang quản trị Admin)
    public ResultPaginationDTO fetchAll(Pageable pageable) {
        Page<Role> pRole = this.roleRepository.findAll(pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pRole.getTotalPages());
        mt.setTotal(pRole.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pRole.getContent());
        return rs;
    }
}