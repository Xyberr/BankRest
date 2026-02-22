package com.example.bankcards.service.transferService;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.TransferStatus;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.exception.ForbiddenException;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.util.MoneyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Instant;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferService implements ITransferService {

    private final CardRepository cardRepository;
    private final TransferRepository transferRepository;
    private final Clock clock;

    @Override
    public void transferBetweenOwnCards(TransferRequest request, Long currentUserId) {

        if (request.getFromCardId().equals(request.getToCardId())) {
            throw new BadRequestException("Cannot transfer to same card");
        }

        BigDecimal amount = MoneyUtil.normalize(request.getAmount());

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Invalid amount");
        }

        Card from = findCardForUpdate(request.getFromCardId());
        Card to = findCardForUpdate(request.getToCardId());

        validateOwnership(currentUserId, from, to);
        validateCardState(from);
        validateCardState(to);

        if (from.getBalance().compareTo(amount) < 0) {
            saveFailedTransfer(from, to, amount);
            throw new BadRequestException("Insufficient funds");
        }

        from.debit(amount);
        to.credit(amount);

        Transfer transfer = Transfer.builder()
                .fromCard(from)
                .toCard(to)
                .amount(amount)
                .status(TransferStatus.SUCCESS)
                .createdAt(Instant.now(clock))
                .build();

        transferRepository.save(transfer);
    }

    private Card findCardForUpdate(Long id) {
        return cardRepository.findByIdForUpdate(id)
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
            throw new BadRequestException("Card is not active");
        }

        if (card.getExpiry().isBefore(LocalDate.now(clock))) {
            card.setStatus(CardStatus.EXPIRED);
            throw new BadRequestException("Card expired");
        }
    }

    private void saveFailedTransfer(Card from, Card to, BigDecimal amount) {

        Transfer transfer = Transfer.builder()
                .fromCard(from)
                .toCard(to)
                .amount(amount)
                .status(TransferStatus.FAILED)
                .createdAt(Instant.now(clock))
                .build();

        transferRepository.save(transfer);
    }
}