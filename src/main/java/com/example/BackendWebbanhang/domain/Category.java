package com.example.BackendWebbanhang.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;

    private String description;

    private boolean active = true; // Sử dụng để xóa mềm
}