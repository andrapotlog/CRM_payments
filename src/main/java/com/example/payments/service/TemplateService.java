package com.example.payments.service;

import com.example.payments.entity.Card;
import com.example.payments.entity.Template;

import java.util.List;

public interface TemplateService {
    public List<Template> listTemplates(Long userId);
    public void addTemplate(Template template);
    public void removeTemplate(Long id);
}
