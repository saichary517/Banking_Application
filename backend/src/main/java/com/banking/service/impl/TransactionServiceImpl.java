package com.banking.service.impl;

import com.banking.dto.PagedResponse;
import com.banking.dto.TransactionResponse;
import com.banking.dto.TransferRequest;
import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.enums.TransactionType;
import com.banking.exception.AccountNotFoundException;
import com.banking.exception.BadRequestException;
import com.banking.exception.InsufficientBalanceException;
import com.banking.mapper.BankingMapper;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import com.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public void transfer(TransferRequest request) {
        if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
            throw new BadRequestException("Sender and receiver account cannot be same");
        }

        Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(request.getFromAccountNumber()));

        Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException(request.getToAccountNumber()));

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException(fromAccount.getAccountNumber());
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        transactionRepository.save(Transaction.builder()
                .account(fromAccount)
                .counterpartyAccount(toAccount)
                .type(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .postBalance(fromAccount.getBalance())
                .description(request.getDescription() == null ? "Transfer debited" : request.getDescription())
                .build());

        transactionRepository.save(Transaction.builder()
                .account(toAccount)
                .counterpartyAccount(fromAccount)
                .type(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .postBalance(toAccount.getBalance())
                .description(request.getDescription() == null ? "Transfer credited" : request.getDescription())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<TransactionResponse> getTransactions(String accountNumber, TransactionType type,
                                                              int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Transaction> txPage;
        if (accountNumber != null && type != null) {
            txPage = transactionRepository.findByAccount_AccountNumberAndType(accountNumber, type, pageable);
        } else if (accountNumber != null) {
            txPage = transactionRepository.findByAccount_AccountNumber(accountNumber, pageable);
        } else if (type != null) {
            txPage = transactionRepository.findByType(type, pageable);
        } else {
            txPage = transactionRepository.findAll(pageable);
        }

        return PagedResponse.<TransactionResponse>builder()
                .content(txPage.getContent().stream().map(BankingMapper::toTransactionResponse).toList())
                .page(txPage.getNumber())
                .size(txPage.getSize())
                .totalElements(txPage.getTotalElements())
                .totalPages(txPage.getTotalPages())
                .last(txPage.isLast())
                .build();
    }
}
