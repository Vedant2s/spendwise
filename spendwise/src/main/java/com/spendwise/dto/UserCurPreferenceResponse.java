package com.spendwise.dto;

public record UserCurPreferenceResponse(
    Long currencyId,
    String currencyCode,
    char symbol) {

}
