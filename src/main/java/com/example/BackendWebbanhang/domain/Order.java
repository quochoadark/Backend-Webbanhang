package com.example.BackendWebbanhang.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import com.example.BackendWebbanhang.util.constant.OrderStatusEnum;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double totalPrice;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;

    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    private Instant createdAt;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
        // Mặc định đơn hàng mới tạo sẽ là PENDING
        if (this.status == null) {
            this.status = OrderStatusEnum.PENDING;
        }
    }
}