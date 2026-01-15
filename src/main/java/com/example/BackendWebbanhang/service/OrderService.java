package com.example.BackendWebbanhang.service;

import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.BackendWebbanhang.domain.*;
import com.example.BackendWebbanhang.repository.*;
import com.example.BackendWebbanhang.util.constant.OrderStatusEnum;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final UserService userService;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository,
            PaymentRepository paymentRepository, CartService cartService,
            ProductRepository productRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.paymentRepository = paymentRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Transactional
    public Order handlePlaceOrder(String email, Order requestOrder) {
        User user = this.userService.handleGetUserByUsername(email);
        List<Cart> cartItems = this.cartService.fetchUserCart(email);

        if (user == null || cartItems == null || cartItems.isEmpty()) {
            return null;
        }

        // 1. Lưu Order
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setReceiverName(requestOrder.getReceiverName());
        newOrder.setReceiverAddress(requestOrder.getReceiverAddress());
        newOrder.setReceiverPhone(requestOrder.getReceiverPhone());
        newOrder.setStatus(OrderStatusEnum.PENDING);

        double total = 0;
        for (Cart item : cartItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        newOrder.setTotalPrice(total);
        newOrder = this.orderRepository.save(newOrder);

        // 2. Lưu Payment (Mặc định COD)
        Payment payment = new Payment();
        payment.setOrder(newOrder);
        payment.setAmount(total);
        payment.setMethod("COD");
        payment.setStatus("PENDING");
        payment.setPaymentTime(Instant.now());
        this.paymentRepository.save(payment);

        // 3. Lưu OrderDetails & Trừ kho
        for (Cart item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(newOrder);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getProduct().getPrice());
            detail.setProductName(item.getProduct().getName());
            this.orderDetailRepository.save(detail);

            Product p = item.getProduct();
            p.setQuantity(p.getQuantity() - item.getQuantity());
            this.productRepository.save(p);
        }

        // 4. Dọn giỏ hàng
        this.cartService.emptyCart(user);
        return newOrder;
    }

    public List<Order> fetchUserOrders(String email) {
        User user = this.userService.handleGetUserByUsername(email);
        return this.orderRepository.findByUser(user);
    }
}