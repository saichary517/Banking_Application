package com.banking.service.impl;

import com.banking.dto.AccountCreateRequest;
import com.banking.dto.AccountResponse;
import com.banking.dto.AmountRequest;
import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.enums.TransactionType;
import com.banking.exception.AccountNotFoundException;
import com.banking.exception.DuplicateResourceException;
import com.banking.exception.InsufficientBalanceException;
import com.banking.mapper.BankingMapper;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import com.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public AccountResponse createAccount(AccountCreateRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .balance(request.getInitialDeposit())
                .build();

        Account saved = accountRepository.save(account);

        Transaction openingTx = Transaction.builder()
                .account(saved)
                .type(TransactionType.DEPOSIT)
                .amount(request.getInitialDeposit())
                .postBalance(saved.getBalance())
                .description("Opening deposit")
                .build();

        transactionRepository.save(openingTx);
        return BankingMapper.toAccountResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountByNumber(String accountNumber) {
        return BankingMapper.toAccountResponse(fetchAccount(accountNumber));
    }

    @Override
    @Transactional
    public AccountResponse deposit(String accountNumber, AmountRequest request) {
        Account account = fetchAccount(accountNumber);
        account.setBalance(account.getBalance().add(request.getAmount()));

        transactionRepository.save(Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .postBalance(account.getBalance())
                .description(request.getDescription())
                .build());

        return BankingMapper.toAccountResponse(account);
    }

    @Override
    @Transactional
    public AccountResponse withdraw(String accountNumber, AmountRequest request) {
        Account account = fetchAccount(accountNumber);
        validateSufficientFunds(account, request.getAmount());
        account.setBalance(account.getBalance().subtract(request.getAmount()));

        transactionRepository.save(Transaction.builder()
                .account(account)
                .type(TransactionType.WITHDRAWAL)
                .amount(request.getAmount())
                .postBalance(account.getBalance())
                .description(request.getDescription())
                .build());

        return BankingMapper.toAccountResponse(account);
    }

    private Account fetchAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    private void validateSufficientFunds(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(account.getAccountNumber());
        }
    }

    private String generateAccountNumber() {
        return "BA" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}
