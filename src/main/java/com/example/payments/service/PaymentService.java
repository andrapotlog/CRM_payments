package com.example.payments.service;

import com.example.payments.entity.Payment;

import java.util.List;

public interface PaymentService {
    public List<Payment> listAllPayments(Long userId);
    public Payment findPayment(Long id);
    public void addPayment(Payment payment) throws Exception;
}
