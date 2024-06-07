package com.example.payments.repository;

import com.example.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
    public List<Payment> findByPayerUserId(Long userId);
}
