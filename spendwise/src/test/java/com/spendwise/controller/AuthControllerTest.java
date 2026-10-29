package com.spendwise.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.spendwise.dto.AuthenticationResponse;
import com.spendwise.dto.LoginRequest;
import com.spendwise.dto.RegisterRequest;
import com.spendwise.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .build();
    }

    @Test
    void postLoginCred_shouldReturnOkAndAuthenticationResponse()
            throws Exception {

        // Arrange
        AuthenticationResponse serviceResponse =
                AuthenticationResponse.builder()
                        .isRegistered(true)
                        .token("login-jwt-token")
                        .build();

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(serviceResponse);

        String requestBody = """
                {
                    "email": "vedant@gmail.com",
                    "passwordHash": "password123"
                }
                """;

        // Act + Assert
        mockMvc.perform(
                        post("/auth/v1/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token")
                        .value("login-jwt-token"));

        ArgumentCaptor<LoginRequest> requestCaptor =
                ArgumentCaptor.forClass(LoginRequest.class);

        verify(authService).login(requestCaptor.capture());

        LoginRequest capturedRequest = requestCaptor.getValue();

        /*
         * This verifies that Spring correctly converted
         * the incoming JSON into LoginRequest.
         */
        org.junit.jupiter.api.Assertions.assertEquals(
                "vedant@gmail.com",
                capturedRequest.email()
        );

        org.junit.jupiter.api.Assertions.assertEquals(
                "password123",
                capturedRequest.passwordHash()
        );
    }

    @Test
    void postRegistringCred_shouldReturnCreatedAndAuthenticationResponse()
            throws Exception {

        // Arrange
        AuthenticationResponse serviceResponse =
                AuthenticationResponse.builder()
                        .isRegistered(true)
                        .token("registration-jwt-token")
                        .build();

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(serviceResponse);

        String requestBody = """
                {
                    "username": "Vedant",
                    "email": "vedant@gmail.com",
                    "passwordHash": "password123",
                    "monthlyIncome": 50000
                }
                """;

        // Act + Assert
        mockMvc.perform(
                        post("/auth/v1/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token")
                        .value("registration-jwt-token"));

        ArgumentCaptor<RegisterRequest> requestCaptor =
                ArgumentCaptor.forClass(RegisterRequest.class);

        verify(authService).register(requestCaptor.capture());

        RegisterRequest capturedRequest = requestCaptor.getValue();

        /*
         * These assertions verify request-body deserialization.
         */
        org.junit.jupiter.api.Assertions.assertEquals(
                "Vedant",
                capturedRequest.username()
        );

        org.junit.jupiter.api.Assertions.assertEquals(
                "vedant@gmail.com",
                capturedRequest.email()
        );

        org.junit.jupiter.api.Assertions.assertEquals(
                "password123",
                capturedRequest.passwordHash()
        );

        org.junit.jupiter.api.Assertions.assertEquals(
                0,
                new java.math.BigDecimal("50000")
                        .compareTo(capturedRequest.monthlyIncome())
        );
    }
}