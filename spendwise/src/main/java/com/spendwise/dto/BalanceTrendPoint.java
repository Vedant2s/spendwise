package com.spendwise.dto;

import java.time.LocalDate;

import lombok.Builder;

import java.math.BigDecimal;

@Builder 
public record BalanceTrendPoint(
        LocalDate date,
        BigDecimal balance
) {}