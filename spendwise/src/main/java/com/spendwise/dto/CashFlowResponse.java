package com.spendwise.dto;

import java.math.BigDecimal;

import lombok.Builder;
@Builder
public record CashFlowResponse(
    BigDecimal income,
    BigDecimal expense,
    BigDecimal cashFlow,
    Double incomeChangePercentage,
    Double cashFlowChangePercentage
) {

}
// {
//   "income": 45000,
//   "expense": 32000,
//   "cashFlow": 13000,
//   "incomeChangePercentage": 12.5,
//   "expenseChangePercentage": -4.2,
//   "cashFlowChangePercentage": 28.7
// }