package com.spendwise.dto;
import java.math.BigDecimal;

import lombok.Builder;
@Builder 
public record CategorywiseSpending(
    String categoryName,
    BigDecimal amount,
    double percentage
){

}
