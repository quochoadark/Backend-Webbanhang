package com.example.BackendWebbanhang.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.BackendWebbanhang.domain.Cart;
import com.example.BackendWebbanhang.service.CartService;
import com.example.BackendWebbanhang.util.SecurityUtil;
import com.example.BackendWebbanhang.util.annotation.ApiMessage;
import com.example.BackendWebbanhang.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts")
    @ApiMessage("Add product to cart")
    public ResponseEntity<Cart> addToCart(@RequestBody Cart reqCart) throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : null;

        Cart cart = this.cartService.handleAddProductToCart(
                email,
                reqCart.getProduct().getId(),
                reqCart.getQuantity());

        if (cart == null) {
            throw new IdInvalidException("Sản phẩm hoặc người dùng không hợp lệ");
        }

        return ResponseEntity.ok(cart);
    }

    @GetMapping("/carts")
    @ApiMessage("Get user cart")
    public ResponseEntity<List<Cart>> getCart() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : null;
        return ResponseEntity.ok(this.cartService.fetchUserCart(email));
    }

    @DeleteMapping("/carts/{id}")
    @ApiMessage("Remove item from cart")
    public ResponseEntity<Void> removeItem(@PathVariable("id") long id) {
        this.cartService.handleDeleteCartItem(id);
        return ResponseEntity.ok(null);
    }
}