package com.spendwise.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder 
public record BalanceInAccountsResponse(
        String currencyCode,
        char symbol,
        Long accountId,
        String accountName,
        BigDecimal balance) {

}
