package com.spendwise.dto;

import lombok.Builder;

@Builder 
public record AuthenticationResponse(boolean isRegistered,String token) {

}
