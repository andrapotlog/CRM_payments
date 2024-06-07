package com.example.payments.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cards")
@Getter
@Setter
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="card_id")
    private Long card_id;

    @Column(name="card_number")
    private String card_number;
    @Column(name="expiration")
    private String expiration;
    @Column(name="card_type")
    private String cardType;
    @Column(name="owner_name")
    private String ownerName;
    @Column(name="saved_by_user_id")
    private Long savedByUserId;
}
