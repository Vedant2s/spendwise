package com.spendwise.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;

@Builder 
public record GoalCreationRequest(String name,BigDecimal targetAmount,BigDecimal currentAmount,LocalDate desired_date) {

}
