package com.example.bankcards.service.transferService;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.dto.transfer.TransferResponse;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface ITransferService {

    void transferBetweenOwnCards(TransferRequest request, Long currentUserId);
    Page<TransferResponse> getUserTransfers(Long userId, Pageable pageable);
}