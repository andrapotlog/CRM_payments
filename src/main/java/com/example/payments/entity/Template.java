package com.example.payments.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "templates")
@Getter
@Setter
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="template_id")
    private Long template_id;

    @Column(name="recipient_name")
    private String recipientName;
    @Column(name="recipient_account_number")
    private String recipientAccountNumber;
    @Column(name="template_name")
    private String templateName;
    @Column(name="created_by_user_id")
    private Long createdByUserId;
}
