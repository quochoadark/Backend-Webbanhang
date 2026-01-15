package com.example.BackendWebbanhang.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.BackendWebbanhang.domain.Order;
import com.example.BackendWebbanhang.service.OrderService;
import com.example.BackendWebbanhang.util.SecurityUtil;
import com.example.BackendWebbanhang.util.annotation.ApiMessage;
import com.example.BackendWebbanhang.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    @ApiMessage("Place a new order")
    public ResponseEntity<Order> placeOrder(@RequestBody Order requestOrder) throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().orElse(null);
        Order order = this.orderService.handlePlaceOrder(email, requestOrder);
        if (order == null) {
            throw new IdInvalidException("Không thể đặt hàng. Giỏ hàng trống hoặc User không tồn tại.");
        }
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders")
    @ApiMessage("Get order history for current user")
    public ResponseEntity<List<Order>> getOrderHistory() {
        String email = SecurityUtil.getCurrentUserLogin().orElse(null);
        return ResponseEntity.ok(this.orderService.fetchUserOrders(email));
    }
}