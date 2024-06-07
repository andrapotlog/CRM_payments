package com.example.payments.service;

import com.example.payments.entity.Card;
import com.example.payments.entity.Template;
import com.example.payments.repository.TemplateRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateServiceImpl implements TemplateService{
    @Autowired
    private TemplateRepo templateRepository;

    @Override
    public List<Template> listTemplates(Long userId) {
        return templateRepository.findByCreatedByUserId(userId);
    }

    @Override
    public void addTemplate(Template template) {
//        Optional<Card> existingCard = cardRepository.findById(cardInformation.getCard_id());
//
//        if (existingCard.isPresent()) {
//            BeanUtils.copyProperties(cardInformation, existingCard.get(), "card_id");
//            cardRepository.saveAndFlush(existingCard.get());
//        }
//        else {
            templateRepository.save(template);
       // }
    }

    @Override
    public void removeTemplate(Long id) {
        templateRepository.deleteById(id);
    }

}
