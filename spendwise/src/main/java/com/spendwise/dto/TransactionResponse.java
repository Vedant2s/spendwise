package com.spendwise.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.spendwise.model.TransactionType;

import lombok.Builder;

@Builder
public record TransactionResponse(
    String category,
    BigDecimal amount,
    String currCode,
    LocalDate date,
    TransactionType type
) {

}
