package com.example.payments.service;

import com.example.payments.entity.Card;
import com.example.payments.repository.CardRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepo cardRepository;

    @Override
    public List<Card> listAllCards(Long userId) {
        return cardRepository.findBySavedByUserId(userId);
    }

    @Override
    public void addCard(Card cardInformation) {
        Optional<Card> existingCard = cardRepository.findById(cardInformation.getCard_id());

        if (existingCard.isPresent()) {
            BeanUtils.copyProperties(cardInformation, existingCard.get(), "card_id");
            cardRepository.saveAndFlush(existingCard.get());
        }
        else {
            cardRepository.save(cardInformation);
        }
    }

    @Override
    public void removeCard(Long id) {
        cardRepository.deleteById(id);
    }
}
