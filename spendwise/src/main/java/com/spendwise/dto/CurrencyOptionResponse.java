package com.spendwise.dto;

import java.math.BigDecimal;


public record CurrencyOptionResponse(String currencyCode,
        char symbol,
        BigDecimal exchangeRate) {

}
