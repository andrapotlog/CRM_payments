package com.example.payments.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payment_id")
    private Long payment_id;

    @Column(name="recipient_name")
    private String recipientName;
    @Column(name="recipient_account_number")
    private String recipientAccountNumber;

    @Column(name="bill_number")
    private String billNumber;
    @Column(name="amount")
    private BigDecimal amount;
    @Column(name="description")
    private String description;

    @Column(name="date_and_time")
    private LocalDateTime dateAndTime = LocalDateTime.now();;

    @Column(name="card_number")
    private String cardNumber;
    @Column(name="expiration")
    private String expiration;
    @Column(name="card_type")
    private String cardType;
    @Column(name="owner_name")
    private String ownerName;

    @Column(name="invoice_id")
    private String invoiceId;
    @Column(name="payer_user_id")
    private Long payerUserId;
}

