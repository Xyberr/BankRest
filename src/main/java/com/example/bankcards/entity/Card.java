package com.example.bankcards.entity;

import com.example.bankcards.entity.converter.CardNumberConverter;
import com.example.bankcards.entity.enums.CardStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cards", indexes = {@Index(name = "idx_card_owner", columnList = "owner_id")})
@Getter
@Setter
public class Card extends BaseEntity {

    @Convert(converter = CardNumberConverter.class)
    @Column(nullable = false, length = 512)
    private String number;

    @Column(nullable = false)
    private LocalDate expiry;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;
}