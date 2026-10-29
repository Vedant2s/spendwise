package com.spendwise.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

import com.spendwise.dto.UpdateCurPreferenceRequest;
import com.spendwise.dto.UserCurPreferenceResponse;
import com.spendwise.service.CurrencyService;
import com.spendwise.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CurrencyService currService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .build();
    }

    @Test
    void setCurrencyPreference_shouldReturnOkAndCurrencyPreference()
            throws Exception {

        // Arrange
        UserCurPreferenceResponse serviceResponse =
                UserCurPreferenceResponse.builder()
                        .currencyId(1L)
                        .currencyCode("INR")
                        .symbol('₹')
                        .build();

        when(userService.setCurrForUser(
                any(UpdateCurPreferenceRequest.class)
        )).thenReturn(serviceResponse);

        String requestBody = """
                {
                    "currencyId": 1
                }
                """;

        // Act + Assert
        mockMvc.perform(
                        patch("/users/me/preferences")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.currencyId")
                        .value(1))

                .andExpect(jsonPath("$.currencyCode")
                        .value("INR"))

                .andExpect(jsonPath("$.symbol")
                        .value("₹"));

        /*
         * Capture the request passed to UserService
         * and verify JSON deserialization.
         */
        ArgumentCaptor<UpdateCurPreferenceRequest> requestCaptor =
                ArgumentCaptor.forClass(
                        UpdateCurPreferenceRequest.class
                );

        verify(userService)
                .setCurrForUser(requestCaptor.capture());

        UpdateCurPreferenceRequest capturedRequest =
                requestCaptor.getValue();

        assertEquals(
                1L,
                capturedRequest.currencyId()
        );
    }
}