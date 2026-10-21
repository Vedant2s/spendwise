package com.spendwise.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;

@Builder
public record SpendingStructureResponse(
    BigDecimal totalSpending,
    List <CategorywiseSpending> categories,
    Double changeFromPreviousPeriodPercentage
) {

}
