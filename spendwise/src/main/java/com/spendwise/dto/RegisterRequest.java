package com.spendwise.dto;

import java.math.BigDecimal;

public record RegisterRequest(
        String username,
        String email,
        String passwordHash,
        BigDecimal monthlyIncome) {

}
