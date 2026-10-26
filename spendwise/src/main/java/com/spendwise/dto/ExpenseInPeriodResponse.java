package com.spendwise.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder 
public record ExpenseInPeriodResponse(
    String categoryName,
    BigDecimal amount,
    String currCode,
    Double percentage
) {

}
