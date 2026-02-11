package com.banking.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
public class AccountResponse {
    Long id;
    String accountNumber;
    String fullName;
    String email;
    BigDecimal balance;
    LocalDateTime createdAt;
}
