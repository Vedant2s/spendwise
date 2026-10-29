package com.spendwise.dto;

import lombok.Builder;

@Builder
public record UserCurPreferenceResponse(
    Long currencyId,
    String currencyCode,
    char symbol) {

}
