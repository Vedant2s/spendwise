package com.spendwise.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record BalanceOfUserResponse(
    BigDecimal currentBalance,
    Double percentChangeInBalance
) {

}
