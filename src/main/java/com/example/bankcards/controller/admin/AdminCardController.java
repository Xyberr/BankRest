package com.example.bankcards.controller.admin;

import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.service.cardService.ICardService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/cards")
@RequiredArgsConstructor
public class AdminCardController {

    private final ICardService cardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponseDTO create(@RequestParam @NotNull Long userId) {
        return cardService.createCard(userId);
    }

    @GetMapping
    public Page<CardResponseDTO> getAll(Pageable pageable) {
        return cardService.getAllCards(pageable);
    }

    @PostMapping("/{cardId}/block")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void block(@PathVariable Long cardId) {
        cardService.blockCard(cardId);
    }

    @PostMapping("/{cardId}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@PathVariable Long cardId) {
        cardService.activateCard(cardId);
    }

    @DeleteMapping("/{cardId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
    }
}