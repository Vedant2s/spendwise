package com.spendwise.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;

@Builder
public record BalanceTrendResponse(
        BigDecimal actualBalance,
        BigDecimal pastBalance,
        Double changePercentage,
        List<BalanceTrendPoint> trend
) {

}