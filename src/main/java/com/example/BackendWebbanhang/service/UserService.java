package com.example.BackendWebbanhang.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.BackendWebbanhang.domain.User;
import com.example.BackendWebbanhang.domain.Role;
import com.example.BackendWebbanhang.domain.response.user.ResCreateUserDTO;
import com.example.BackendWebbanhang.domain.response.user.ResUpdateUserDTO;
import com.example.BackendWebbanhang.domain.response.user.ResUserDTO;
import com.example.BackendWebbanhang.domain.response.user.ResultPaginationDTO;
import com.example.BackendWebbanhang.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public User handleCreateUser(User user) {
        // 1. Xử lý gán Role khi Admin tạo
        if (user.getRole() != null && user.getRole().getId() > 0) {
            // Lấy Role từ Database dựa trên ID truyền lên
            Role r = this.roleService.fetchById(user.getRole().getId());
            if (r != null) {
                user.setRole(r);
            } else {
                // Nếu ID role không tồn tại, gán mặc định là USER
                user.setRole(this.roleService.fetchByName("USER"));
            }
        } else {
            // Nếu không truyền role, tự động gán là USER
            user.setRole(this.roleService.fetchByName("USER"));
        }

        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        return userOptional.orElse(null);
    }

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(this::convertToResUserDTO)
                .collect(Collectors.toList());

        rs.setResult(listUser);
        return rs;
    }

    public User handleUpdateUser(User reqUser) {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (currentUser != null) {
            currentUser.setAddress(reqUser.getAddress());
            currentUser.setGender(reqUser.getGender());
            currentUser.setAge(reqUser.getAge());
            currentUser.setName(reqUser.getName());

            // 2. Cập nhật Role nếu Admin thay đổi
            if (reqUser.getRole() != null && reqUser.getRole().getId() > 0) {
                Role r = this.roleService.fetchById(reqUser.getRole().getId());
                if (r != null) {
                    currentUser.setRole(r);
                }
            }

            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        // Mapping Role cho Response trả về
        if (user.getRole() != null) {
            ResCreateUserDTO.RoleUser roleRes = new ResCreateUserDTO.RoleUser();
            roleRes.setId(user.getRole().getId());
            roleRes.setName(user.getRole().getName());
            res.setRole(roleRes);
        }
        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        // Thêm Role vào DTO Update nếu cần hiển thị ở Frontend
        if (user.getRole() != null) {
            ResUpdateUserDTO.RoleUser roleRes = new ResUpdateUserDTO.RoleUser();
            roleRes.setId(user.getRole().getId());
            roleRes.setName(user.getRole().getName());
            res.setRole(roleRes);
        }
        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());

        if (user.getRole() != null) {
            ResUserDTO.RoleUser roleRes = new ResUserDTO.RoleUser();
            roleRes.setId(user.getRole().getId());
            roleRes.setName(user.getRole().getName());
            res.setRole(roleRes);
        }
        return res;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}