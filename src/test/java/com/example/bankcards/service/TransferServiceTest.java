package com.example.bankcards.service;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.entity.enums.TransferStatus;
import com.example.bankcards.exception.BadRequestException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.service.transferService.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private TransferRepository transferRepository;

    private final Clock fixedClock =
            Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);

    private TransferService transferService;

    @BeforeEach
    void setUp() {
        transferService = new TransferService(cardRepository, transferRepository, fixedClock);
    }

    @Test
    void shouldTransferSuccessfully() {

        User user = createUser(1L);

        Card from = createCard(10L, user, "100.00");
        Card to   = createCard(20L, user, "0.00");

        when(cardRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(from));
        when(cardRepository.findByIdForUpdate(20L)).thenReturn(Optional.of(to));

        TransferRequest request =
                new TransferRequest(10L, 20L, new BigDecimal("50.00"));

        transferService.transferBetweenOwnCards(request, 1L);

        assertEquals(new BigDecimal("50.00"), from.getBalance());
        assertEquals(new BigDecimal("50.00"), to.getBalance());

        ArgumentCaptor<Transfer> captor = ArgumentCaptor.forClass(Transfer.class);
        verify(transferRepository).save(captor.capture());

        Transfer saved = captor.getValue();

        assertEquals(TransferStatus.SUCCESS, saved.getStatus());
        assertEquals(Instant.now(fixedClock), saved.getCreatedAt());
    }

    @Test
    void shouldThrowWhenSameCard() {
        TransferRequest request =
                new TransferRequest(10L, 10L, new BigDecimal("10.00"));

        assertThrows(BadRequestException.class,
                () -> transferService.transferBetweenOwnCards(request, 1L));
    }

    @Test
    void shouldThrowWhenAmountIsZero() {
        TransferRequest request =
                new TransferRequest(10L, 20L, BigDecimal.ZERO);

        assertThrows(BadRequestException.class,
                () -> transferService.transferBetweenOwnCards(request, 1L));
    }

    @Test
    void shouldThrowWhenCardNotFound() {

        when(cardRepository.findByIdForUpdate(10L))
                .thenReturn(Optional.empty());

        TransferRequest request =
                new TransferRequest(10L, 20L, new BigDecimal("10.00"));

        assertThrows(BadRequestException.class,
                () -> transferService.transferBetweenOwnCards(request, 1L));

        verify(transferRepository).save(argThat(t ->
                t.getStatus() == TransferStatus.FAILED
        ));
    }

    @Test
    void shouldThrowWhenInsufficientFunds() {

        User user = createUser(1L);

        Card from = createCard(10L, user, "10.00");
        Card to   = createCard(20L, user, "0.00");

        when(cardRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(from));
        when(cardRepository.findByIdForUpdate(20L)).thenReturn(Optional.of(to));

        TransferRequest request =
                new TransferRequest(10L, 20L, new BigDecimal("50.00"));

        assertThrows(BadRequestException.class,
                () -> transferService.transferBetweenOwnCards(request, 1L));

        verify(transferRepository).save(argThat(t ->
                t.getStatus() == TransferStatus.FAILED
        ));

        verify(transferRepository, never())
                .save(argThat(t -> t.getStatus() == TransferStatus.SUCCESS));
    }

    @Test
    void shouldThrowWhenCardBlocked() {

        User user = createUser(1L);

        Card from = createCard(10L, user, "100.00");
        from.setStatus(CardStatus.BLOCKED);

        Card to = createCard(20L, user, "0.00");

        when(cardRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(from));
        when(cardRepository.findByIdForUpdate(20L)).thenReturn(Optional.of(to));

        TransferRequest request =
                new TransferRequest(10L, 20L, new BigDecimal("10.00"));

        assertThrows(BadRequestException.class,
                () -> transferService.transferBetweenOwnCards(request, 1L));

        verify(transferRepository).save(argThat(t ->
                t.getStatus() == TransferStatus.FAILED
        ));
    }

    @Test
    void shouldThrowWhenCardBelongsToAnotherUser() {

        User owner = createUser(2L);

        Card from = createCard(10L, owner, "100.00");
        Card to   = createCard(20L, owner, "0.00");

        when(cardRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(from));
        when(cardRepository.findByIdForUpdate(20L)).thenReturn(Optional.of(to));

        TransferRequest request =
                new TransferRequest(10L, 20L, new BigDecimal("10.00"));

        assertThrows(BadRequestException.class,
                () -> transferService.transferBetweenOwnCards(request, 1L));
    }

    private User createUser(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private Card createCard(Long id, User owner, String balance) {
        Card card = new Card();
        card.setId(id);
        card.setOwner(owner);
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(new BigDecimal(balance));
        return card;
    }
}