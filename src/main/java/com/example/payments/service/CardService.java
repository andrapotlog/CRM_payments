package com.example.payments.service;

import com.example.payments.entity.Card;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;

public interface CardService {
    public List<Card> listAllCards(Long userId);
    public void addCard(Card cardInformation);
    public void removeCard(Long id);
}
