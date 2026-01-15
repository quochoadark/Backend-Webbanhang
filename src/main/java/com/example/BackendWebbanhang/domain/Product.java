package com.example.BackendWebbanhang.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private double price;
    private long quantity;
    private String image;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private boolean active = true; // Xóa mềm: Khi xóa sản phẩm, set active = false

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}