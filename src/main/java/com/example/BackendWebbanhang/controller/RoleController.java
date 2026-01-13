package com.example.BackendWebbanhang.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.BackendWebbanhang.domain.Role;
import com.example.BackendWebbanhang.service.RoleService;
import com.example.BackendWebbanhang.util.annotation.ApiMessage;
import com.example.BackendWebbanhang.util.error.IdInvalidException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a role")
    public ResponseEntity<Role> create(@Valid @RequestBody Role r) throws IdInvalidException {
        // Kiểm tra xem tên role đã tồn tại chưa
        if (this.roleService.existsByName(r.getName())) {
            throw new IdInvalidException("Role với name = " + r.getName() + " đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(r));
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> update(@RequestBody Role r) throws IdInvalidException {
        // Kiểm tra id có tồn tại không trước khi update
        Role currentRole = this.roleService.fetchById(r.getId());
        if (currentRole == null) {
            throw new IdInvalidException("Role với id = " + r.getId() + " không tồn tại");
        }
        return ResponseEntity.ok().body(this.roleService.update(r));
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch role by id")
    public ResponseEntity<Role> getById(@PathVariable("id") long id) throws IdInvalidException {
        Role r = this.roleService.fetchById(id);
        if (r == null) {
            throw new IdInvalidException("Role với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok().body(r);
    }

    @GetMapping("/roles")
    @ApiMessage("Fetch roles with pagination")
    public ResponseEntity<Object> getRoles(Pageable pageable) {
        return ResponseEntity.ok().body(this.roleService.fetchAll(pageable));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        Role r = this.roleService.fetchById(id);
        if (r == null) {
            throw new IdInvalidException("Role với id = " + id + " không tồn tại");
        }
        this.roleService.delete(id);
        return ResponseEntity.ok().body(null);
    }
}