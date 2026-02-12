package com.example.bankcards.controller;

import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.service.cardService.ICardService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/cards")
@Validated
@RequiredArgsConstructor
public class CardController {
    private final ICardService cardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponseDTO createCard(@RequestParam @NotNull Long userId) {
        return cardService.createCard(userId);
    }

    @GetMapping
    public Page<CardResponseDTO> getAllCards(Pageable pageable) {
        return cardService.getAllCards(pageable);
    }

    @GetMapping("/user/{userId}")
    public Page<CardResponseDTO> getUserCards(@PathVariable @NotNull Long userId, Pageable pageable) {
        return cardService.getUserCards(userId, pageable);
    }

    @PostMapping("/{cardId}/block")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void blockCard(@PathVariable @NotNull Long cardId) {
        cardService.blockCard(cardId);
    }

    @PostMapping("/{cardId}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateCard(@PathVariable @NotNull Long cardId) {
        cardService.activateCard(cardId);
    }

    @DeleteMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(@PathVariable @NotNull Long cardId) {
        cardService.deleteCard(cardId);
    }

    @PostMapping("/request-block")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void requestBlock(@RequestParam @NotNull Long userId, @RequestParam @NotNull Long cardId) {
        cardService.requestBlock(userId, cardId);
    }
}