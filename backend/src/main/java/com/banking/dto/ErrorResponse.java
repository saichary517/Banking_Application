package com.banking.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class ErrorResponse {
    LocalDateTime timestamp;
    int status;
    String error;
    String message;
    List<String> details;
}
