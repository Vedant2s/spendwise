package com.spendwise.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record CreateAccountRequest(String name, Type type,
        String accountNumber, BigDecimal initialBalance) {

}
