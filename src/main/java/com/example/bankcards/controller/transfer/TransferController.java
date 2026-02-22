package com.example.bankcards.controller.transfer;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.service.transferService.ITransferService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/transfers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class TransferController {

    private final ITransferService transferService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transfer(
            @Valid @RequestBody TransferRequest request,
            Authentication authentication
    ) {

        Long currentUserId = Long.parseLong(authentication.getName());

        transferService.transferBetweenOwnCards(request, currentUserId);
    }
}