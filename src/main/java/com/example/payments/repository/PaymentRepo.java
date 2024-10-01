package com.example.payments.repository;

import com.example.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
    public List<Payment> findByPayerUserIdOrderByDateAndTimeDesc(Long userId);
    public List<Payment> findAllByOrderByDateAndTimeDesc();
    public Optional<Payment> findByInvoiceId(String invoiceId);
}
