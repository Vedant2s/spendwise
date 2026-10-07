package com.spendwise.dto;

import java.math.BigDecimal;

public record BalanceInCurrencyResponse(
                Long currencyId,
                String currencyCode,
                char symbol,
                BigDecimal value) {

}
