package com.example.bankcards.service.transferService;


import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.exception.ForbiddenException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.util.MoneyUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.TransferStatus;
import com.example.bankcards.repository.TransferRepository;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferService implements ITransferService {

    private final CardRepository cardRepository;
    private final TransferRepository transferRepository;
    private final Clock clock;

    @Override
    public void transferBetweenOwnCards(TransferRequest request) {

        if (request.getFromCardId().equals(request.getToCardId())) {
            throw new IllegalArgumentException("Cannot transfer to same card");
        }

        Card from = findCard(request.getFromCardId());
        Card to = findCard(request.getToCardId());

        validateOwnership(request.getUserId(), from, to);
        validateCardState(from);
        validateCardState(to);

        var amount = MoneyUtil.normalize(request.getAmount());

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        from.setBalance(MoneyUtil.normalize(from.getBalance().subtract(amount)));
        to.setBalance(MoneyUtil.normalize(to.getBalance().add(amount)));

        Transfer transfer = new Transfer();
        transfer.setFromCard(from);
        transfer.setToCard(to);
        transfer.setAmount(amount);
        transfer.setCreatedAt(Instant.now(clock));
        transfer.setStatus(TransferStatus.SUCCESS);

        transferRepository.save(transfer);
    }

    private Card findCard(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Card not found: " + id));
    }

    private void validateOwnership(Long userId, Card from, Card to) {

        if (!from.getOwner().getId().equals(userId) ||
                !to.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Cards do not belong to user");
        }
    }

    private void validateCardState(Card card) {

        if (card.getStatus() != CardStatus.ACTIVE) {
            throw new IllegalStateException("Card is not active");
        }

        if (card.getExpiry().isBefore(LocalDate.now(clock))) {
            card.setStatus(CardStatus.EXPIRED);
            throw new IllegalStateException("Card expired");
        }
    }
}