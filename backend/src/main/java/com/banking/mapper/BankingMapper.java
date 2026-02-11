package com.banking.mapper;

import com.banking.dto.AccountResponse;
import com.banking.dto.TransactionResponse;
import com.banking.entity.Account;
import com.banking.entity.Transaction;

public final class BankingMapper {

    private BankingMapper() {
    }

    public static AccountResponse toAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .fullName(account.getFullName())
                .email(account.getEmail())
                .balance(account.getBalance())
                .createdAt(account.getCreatedAt())
                .build();
    }

    public static TransactionResponse toTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .accountNumber(transaction.getAccount().getAccountNumber())
                .counterpartyAccountNumber(transaction.getCounterpartyAccount() != null
                        ? transaction.getCounterpartyAccount().getAccountNumber()
                        : null)
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .postBalance(transaction.getPostBalance())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
