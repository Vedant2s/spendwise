package com.spendwise.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;

@Builder
public record GoalCreationResponse(Long goalId,String name,BigDecimal targetAmount,BigDecimal currentAmount,LocalDate desiredDate) {

}
