package com.example.bankcards.service.cardService;

import com.example.bankcards.dto.card.CardResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ICardService {
    CardResponseDTO createCard(Long userId);

    void blockCard(Long cardId);

    void activateCard(Long cardId);

    void deleteCard(Long cardId);

    Page<CardResponseDTO> getAllCards(Pageable pageable);

    Page<CardResponseDTO> getUserCards(Long userId, Pageable pageable);

    void requestBlock(Long userId, Long cardId);
}
