package com.example.bankcards.service.transferService;

import com.example.bankcards.dto.transfer.TransferRequest;

public interface ITransferService {

    void transferBetweenOwnCards(TransferRequest request, Long currentUserId);
}