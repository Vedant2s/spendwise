package com.spendwise.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.spendwise.dto.CurrencyOptionResponse;
import com.spendwise.service.CurrencyService;

@ExtendWith(MockitoExtension.class)
class CurrencyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CurrencyService currService;

    @InjectMocks
    private CurrencyController currencyController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(currencyController)
                .build();
    }

    @Test
    void getCurrencyOptionList_shouldReturnOkAndCurrencies()
            throws Exception {

        // Arrange
        CurrencyOptionResponse inr =
                new CurrencyOptionResponse(
                        "INR",
                        '₹',
                        new BigDecimal("1.00")
                );

        CurrencyOptionResponse usd =
                new CurrencyOptionResponse(
                        "USD",
                        '$',
                        new BigDecimal("0.012")
                );

        when(currService.getAll())
                .thenReturn(List.of(inr, usd));

        // Act + Assert
        mockMvc.perform(
                        get("/currency")
                                .param("param", "all")
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.length()")
                        .value(2))

                .andExpect(jsonPath("$[0].currencyCode")
                        .value("INR"))
                .andExpect(jsonPath("$[0].symbol")
                        .value("₹"))
                .andExpect(jsonPath("$[0].exchangeRate")
                        .value(1.00))

                .andExpect(jsonPath("$[1].currencyCode")
                        .value("USD"))
                .andExpect(jsonPath("$[1].symbol")
                        .value("$"))
                .andExpect(jsonPath("$[1].exchangeRate")
                        .value(0.012));

        verify(currService).getAll();
    }

    @Test
    void getCurrencyOptionList_shouldReturnEmptyArray_whenNoCurrenciesExist()
            throws Exception {

        // Arrange
        when(currService.getAll())
                .thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(
                        get("/currency")
                                .param("param", "all")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()")
                        .value(0));

        verify(currService).getAll();
    }
}