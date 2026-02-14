package com.example.bankcards.mapper;


import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.util.MaskUtil;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public CardResponseDTO toDto(Card card) {
        if (card == null) return null;

        return CardResponseDTO.builder()
                .id(card.getId())
                .number(mask(card.getNumber()))
                .balance(card.getBalance())
                .status(card.getStatus())
                .build();
    }

    public Card toEntity(CardResponseDTO dto) {
        if (dto == null) return null;

        Card card = new Card();
        card.setNumber(dto.getNumber());
        card.setBalance(dto.getBalance());
        card.setStatus(dto.getStatus());
        return card;
    }

    private String mask(String number) {
        return MaskUtil.maskCard(number);
    }
}