package com.example.BackendWebbanhang.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String method; // VNPAY, MOMO, COD, BANK_TRANSFER
    private String status; // PENDING, SUCCESS, FAILED, CANCELLED
    private double amount;
    private String transactionId; // Mã giao dịch từ phía Ngân hàng/Cổng thanh toán
    private String paymentInfo; // Nội dung thanh toán
    private Instant paymentTime;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}