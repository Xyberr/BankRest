package com.example.bankcards.controller.user;

import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.dto.transfer.TransferResponse;
import com.example.bankcards.service.cardService.ICardService;
import com.example.bankcards.service.transferService.ITransferService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user/cards")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class UserCardController {

    private final ICardService cardService;
    private final ITransferService transferService;

    @GetMapping("/{userId}")
    public Page<CardResponseDTO> myCards(
            @PathVariable @NotNull Long userId,
            Pageable pageable
    ) {
        return cardService.getUserCards(userId, pageable);
    }

    @PostMapping("/{cardId}/request-block")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void requestBlock(
            @RequestParam Long userId,
            @PathVariable Long cardId
    ) {
        cardService.requestBlock(userId, cardId);
    }

    @GetMapping
    public Page<TransferResponse> getTransfers(
            Pageable pageable,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        return transferService.getUserTransfers(userId, pageable);
    }
}