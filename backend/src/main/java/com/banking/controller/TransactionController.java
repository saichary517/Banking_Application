package com.banking.controller;

import com.banking.dto.PagedResponse;
import com.banking.dto.TransactionResponse;
import com.banking.dto.TransferRequest;
import com.banking.enums.TransactionType;
import com.banking.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction APIs", description = "Operations for transfer and transaction history")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    @Operation(summary = "Transfer money between accounts")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        transactionService.transfer(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    @Operation(summary = "Get transaction history with optional filters")
    public ResponseEntity<PagedResponse<TransactionResponse>> getTransactions(
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction
    ) {
        return ResponseEntity.ok(transactionService.getTransactions(accountNumber, type, page, size, sortBy, direction));
    }
}
