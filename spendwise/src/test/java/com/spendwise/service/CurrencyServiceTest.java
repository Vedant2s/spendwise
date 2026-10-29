package com.spendwise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spendwise.dto.CurrencyOptionResponse;
import com.spendwise.model.Currency;
import com.spendwise.repository.CurrencyRepository;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepos;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void getAll_shouldReturnAllCurrencies() {

        Currency usd = Currency.builder()
                .currencyCode("USD")
                .symbol('$')
                .exchangeRate(new BigDecimal("1.00"))
                .build();

        Currency inr = Currency.builder()
                .currencyCode("INR")
                .symbol('₹')
                .exchangeRate(new BigDecimal("83.00"))
                .build();
        when(currencyRepos.findAll())
                .thenReturn(List.of(usd, inr));

        List<CurrencyOptionResponse> result = currencyService.getAll();

        assertEquals(2, result.size());

        assertEquals("USD", result.get(0).currencyCode());
        assertEquals('$', result.get(0).symbol());
        assertEquals(new BigDecimal("1.00"), result.get(0).exchangeRate());

        assertEquals("INR", result.get(1).currencyCode());
        assertEquals('₹', result.get(1).symbol());
        assertEquals(new BigDecimal("83.00"), result.get(1).exchangeRate());

        verify(currencyRepos).findAll();
    }

    @Test
    void getAll_shouldReturnEmptyList_whenNoCurrenciesExist() {

        when(currencyRepos.findAll())
                .thenReturn(List.of());

        List<CurrencyOptionResponse> result = currencyService.getAll();

        assertEquals(0, result.size());

        verify(currencyRepos).findAll();
    }
}
