package com.example.bankcards.service.cardService;

import com.example.bankcards.dto.card.CardResponseDTO;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.NotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardNumberUtil;
import com.example.bankcards.util.MoneyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
@Transactional
public class CardService implements ICardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper mapper;

    @Override
    public CardResponseDTO createCard(Long userId) {

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        Card card = new Card();
        card.setOwner(owner);
        card.setNumber(CardNumberUtil.generate());
        card.setExpiry(LocalDate.now().plusYears(3));
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(MoneyUtil.normalize(card.getBalance()));

        Card saved = cardRepository.save(card);

        return mapper.toDto(saved);
    }

    @Override
    public void blockCard(Long cardId) {
        findCard(cardId).setStatus(CardStatus.BLOCKED);
    }

    @Override
    public void activateCard(Long cardId) {
        findCard(cardId).setStatus(CardStatus.ACTIVE);
    }

    @Override
    public void deleteCard(Long cardId) {
        cardRepository.delete(findCard(cardId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponseDTO> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CardResponseDTO> getUserCards(Long userId, Pageable pageable) {
        return cardRepository.findByOwnerId(userId, pageable)
                .map(mapper::toDto);
    }

    @Override
    public void requestBlock(Long userId, Long cardId) {

        Card card = findCard(cardId);

        if (!card.getOwner().getId().equals(userId)) {
            throw new SecurityException("Access denied");
        }

        card.setStatus(CardStatus.BLOCKED);
    }

    private Card findCard(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Card not found: " + id));
    }
}
