package com.spendwise.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record CategoryBudgetCreationRequest(Long accountId,
                Long categoryId,
                String name,
                BigDecimal maxAllotedBudget) {

}
