package com.banking.dto;

import com.banking.enums.TransactionType;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class TransactionResponse {
    Long id;
    String accountNumber;
    String counterpartyAccountNumber;
    TransactionType type;
    BigDecimal amount;
    BigDecimal postBalance;
    String description;
    LocalDateTime createdAt;
}
