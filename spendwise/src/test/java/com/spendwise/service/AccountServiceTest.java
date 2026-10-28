package com.spendwise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spendwise.config.Role;
import com.spendwise.dto.CreateAccountRequest;
import com.spendwise.dto.CreateAccountResponse;
import com.spendwise.dto.Type;
import com.spendwise.model.Account;
import com.spendwise.model.Currency;
import com.spendwise.model.User;
import com.spendwise.repository.AccountRepository;
import com.spendwise.repository.TransactionRepository;
import com.spendwise.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepo;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private User user;
    private Currency currency;

    @BeforeEach
    public void createAUser() {
        currency = Currency.builder()
                .currencyId(1L)
                .currencyCode("INR")
                .symbol((char) 8377)
                .exchangeRate(BigDecimal.valueOf(99))
                .build();
        user = User.builder()
                .userID(1L)
                .username("Vedant")
                .email("vedant@gmail.com")
                .passwordHash("anything")
                .monthlyIncome(BigDecimal.valueOf(27800))
                .currency(currency)
                .role(Role.USER)
                .build();
    }

    @Test
    void create_shouldCreateCashAccount() {
        String email = "vedant@gmail.com";
        CreateAccountRequest request = CreateAccountRequest.builder()
                .name("Cash")
                .type(Type.CASH)
                .accountNumber("12345678")
                .initialBalance(BigDecimal.valueOf(2300))
                .build();
        when(userRepository.findByEmail(email))
            .thenReturn(Optional.of(user));
        when(accountRepo.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        CreateAccountResponse response=accountService.create(request, email);
        assertEquals("Cash", response.name());
        assertEquals(Type.CASH, response.type());
        assertEquals(new BigDecimal("2300"), response.balance());
        assertEquals("12345678", response.accountNumber());

        verify(userRepository).findByEmail(email);
        verify(accountRepo).save(any(Account.class));
    }
}
