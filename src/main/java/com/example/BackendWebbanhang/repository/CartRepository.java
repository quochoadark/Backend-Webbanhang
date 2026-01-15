package com.example.BackendWebbanhang.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.BackendWebbanhang.domain.Cart;
import com.example.BackendWebbanhang.domain.User;
import com.example.BackendWebbanhang.domain.Product;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);

    Optional<Cart> findByUserAndProduct(User user, Product product);

    void deleteByUser(User user);
}