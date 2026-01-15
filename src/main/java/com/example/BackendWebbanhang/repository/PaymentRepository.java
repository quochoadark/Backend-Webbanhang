package com.example.BackendWebbanhang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.BackendWebbanhang.domain.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}