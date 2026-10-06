package com.spendwise.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record AccountDetailsResponse(Long id, String name, Type type, String accountNumber, BigDecimal balance) {

}
