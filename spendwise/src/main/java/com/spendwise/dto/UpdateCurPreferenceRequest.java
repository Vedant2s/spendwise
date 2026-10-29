package com.spendwise.dto;

import lombok.Builder;

@Builder 
public record UpdateCurPreferenceRequest(Long currencyId) {

}
