package com.banking.service;

import com.banking.dto.PagedResponse;
import com.banking.dto.TransactionResponse;
import com.banking.dto.TransferRequest;
import com.banking.enums.TransactionType;

public interface TransactionService {
    void transfer(TransferRequest request);

    PagedResponse<TransactionResponse> getTransactions(String accountNumber, TransactionType type,
                                                       int page, int size, String sortBy, String direction);
}
