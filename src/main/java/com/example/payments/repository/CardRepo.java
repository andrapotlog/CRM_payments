package com.example.payments.repository;

import com.example.payments.entity.Card;
import com.example.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepo extends JpaRepository<Card, Long> {
    public List<Card> findBySavedByUserId(Long userId);
}
