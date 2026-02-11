package com.banking.controller;

import com.banking.dto.AccountCreateRequest;
import com.banking.dto.AccountResponse;
import com.banking.dto.AmountRequest;
import com.banking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Account APIs", description = "Operations related to account management")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Create a new account")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));
    }

    @GetMapping("/{accountNumber}")
    @Operation(summary = "Get account details by account number")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
    }

    @PostMapping("/{accountNumber}/deposit")
    @Operation(summary = "Deposit amount into an account")
    public ResponseEntity<AccountResponse> deposit(@PathVariable String accountNumber,
                                                   @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(accountService.deposit(accountNumber, request));
    }

    @PostMapping("/{accountNumber}/withdraw")
    @Operation(summary = "Withdraw amount from an account")
    public ResponseEntity<AccountResponse> withdraw(@PathVariable String accountNumber,
                                                    @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(accountService.withdraw(accountNumber, request));
    }
}
