package com.spendwise.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder 
public record RegisterRequest(
        String username,
        String email,
        String passwordHash,
        BigDecimal monthlyIncome) {

}
