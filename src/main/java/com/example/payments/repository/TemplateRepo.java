package com.example.payments.repository;

import com.example.payments.entity.Card;
import com.example.payments.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepo extends JpaRepository<Template, Long> {
    List<Template> findByCreatedByUserId(Long userId);
}
