package com.spendwise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spendwise.dto.UpdateCurPreferenceRequest;
import com.spendwise.dto.UserCurPreferenceResponse;
import com.spendwise.exception.customException.CurrencyNotAvailable;
import com.spendwise.model.Currency;
import com.spendwise.repository.CurrencyRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private CurrencyRepository currencyRepos;

    @InjectMocks
    private UserService userService;


    @Test
    void setCurrForUser_shouldReturnCurrencyPreference_whenCurrencyExists() {

        Long currencyId = 1L;

        UpdateCurPreferenceRequest request =
                new UpdateCurPreferenceRequest(currencyId);

        Currency currency = Currency.builder()
                .currencyId(currencyId)
                .currencyCode("INR")
                .symbol('₹')
                .build();

        when(currencyRepos.findById(currencyId))
                .thenReturn(Optional.of(currency));

        UserCurPreferenceResponse response =
                userService.setCurrForUser(request);

        assertNotNull(response);

        assertEquals(currencyId, response.currencyId());
        assertEquals("INR", response.currencyCode());
        assertEquals('₹', response.symbol());

        verify(currencyRepos).findById(currencyId);
    }


    @Test
    void setCurrForUser_shouldThrowCurrencyNotAvailable_whenCurrencyDoesNotExist() {

        Long currencyId = 999L;

        UpdateCurPreferenceRequest request =
                new UpdateCurPreferenceRequest(currencyId);

        when(currencyRepos.findById(currencyId))
                .thenReturn(Optional.empty());

        assertThrows(
                CurrencyNotAvailable.class,
                () -> userService.setCurrForUser(request)
        );

        verify(currencyRepos).findById(currencyId);
    }
}