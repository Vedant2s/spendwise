package com.spendwise.dto;

import java.math.BigDecimal;

public record CategoryBudgetCreationRequest(Long accountId,
                Long categoryId,
                String name,
                BigDecimal maxAllotedBudget) {

}
