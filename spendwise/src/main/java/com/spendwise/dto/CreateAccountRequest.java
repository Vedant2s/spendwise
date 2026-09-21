package com.spendwise.dto;

import java.math.BigDecimal;

enum Type{
    CASH,
    DEBIT,
    CREDIT
}
public record CreateAccountRequest(String name,Type type, BigDecimal initialBalance) {

}
