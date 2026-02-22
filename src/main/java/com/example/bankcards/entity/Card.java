package com.example.bankcards.entity;

import com.example.bankcards.entity.converter.CardNumberConverter;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.util.MoneyUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

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

    public void debit(BigDecimal amount) {
        this.balance = MoneyUtil.normalize(this.balance.subtract(amount));
    }

    public void credit(BigDecimal amount) {
        this.balance = MoneyUtil.normalize(this.balance.add(amount));
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;
}