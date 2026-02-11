package com.banking.service;

import com.banking.dto.AccountCreateRequest;
import com.banking.dto.AccountResponse;
import com.banking.dto.AmountRequest;

public interface AccountService {
    AccountResponse createAccount(AccountCreateRequest request);

    AccountResponse getAccountByNumber(String accountNumber);

    AccountResponse deposit(String accountNumber, AmountRequest request);

    AccountResponse withdraw(String accountNumber, AmountRequest request);
}
