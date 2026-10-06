package com.spendwise.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder 
public record CategoryBudgetCreationResponse(Long categoryBudgetId, String name,
        BigDecimal maxAllotedBudget) {

}
