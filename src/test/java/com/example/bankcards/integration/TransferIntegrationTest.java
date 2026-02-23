package com.example.bankcards.integration;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.transferService.TransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
class TransferIntegrationTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldTransferMoneyBetweenCards() {

        User user = new User();
        userRepository.save(user);

        Card from = new Card();
        from.setOwner(user);
        from.setStatus(CardStatus.ACTIVE);
        from.setBalance(new BigDecimal("100.00"));
        cardRepository.save(from);

        Card to = new Card();
        to.setOwner(user);
        to.setStatus(CardStatus.ACTIVE);
        to.setBalance(BigDecimal.ZERO);
        cardRepository.save(to);

        TransferRequest request =
                new TransferRequest(from.getId(), to.getId(), new BigDecimal("50.00"));

        transferService.transferBetweenOwnCards(request, user.getId());

        Card updatedFrom = cardRepository.findById(from.getId()).orElseThrow();
        Card updatedTo   = cardRepository.findById(to.getId()).orElseThrow();

        assertEquals(new BigDecimal("50.00"), updatedFrom.getBalance());
        assertEquals(new BigDecimal("50.00"), updatedTo.getBalance());
    }
}