package com.example.BackendWebbanhang.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.BackendWebbanhang.domain.Cart;
import com.example.BackendWebbanhang.domain.User;
import com.example.BackendWebbanhang.domain.Product;
import com.example.BackendWebbanhang.repository.CartRepository;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final UserService userService;

    public CartService(CartRepository cartRepository, ProductService productService, UserService userService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.userService = userService;
    }

    public Cart handleAddProductToCart(String email, long productId, long quantity) {
        User user = this.userService.handleGetUserByUsername(email);
        Product product = this.productService.fetchProductById(productId);

        if (user == null || product == null)
            return null;

        Optional<Cart> cartOptional = this.cartRepository.findByUserAndProduct(user, product);

        if (cartOptional.isPresent()) {
            Cart currentCart = cartOptional.get();
            currentCart.setQuantity(currentCart.getQuantity() + quantity);
            return this.cartRepository.save(currentCart);
        } else {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setProduct(product);
            newCart.setQuantity(quantity);
            return this.cartRepository.save(newCart);
        }
    }

    public List<Cart> fetchUserCart(String email) {
        User user = this.userService.handleGetUserByUsername(email);
        if (user == null)
            return null;
        return this.cartRepository.findByUser(user);
    }

    public void handleDeleteCartItem(long id) {
        this.cartRepository.deleteById(id);
    }

    @Transactional
    public void emptyCart(User user) {
        this.cartRepository.deleteByUser(user);
    }
}