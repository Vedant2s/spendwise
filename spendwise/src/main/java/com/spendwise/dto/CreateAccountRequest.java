package com.spendwise.dto;

import java.math.BigDecimal;

public record CreateAccountRequest(String name, Type type, String accountNumber, BigDecimal initialBalance) {

}
