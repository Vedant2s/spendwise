package com.spendwise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.spendwise.config.Role;
import com.spendwise.dto.AccountDetailsResponse;
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
        CreateAccountResponse response = accountService.create(request, email);
        assertEquals("Cash", response.name());
        assertEquals(Type.CASH, response.type());
        assertEquals(new BigDecimal("2300"), response.balance());
        assertEquals("12345678", response.accountNumber());

        verify(userRepository).findByEmail(email);
        verify(accountRepo).save(any(Account.class));
    }

    @Test
    void create_shouldThrowExceptionWhenUserDoesNotExist() {

        String email = "unknown@test.com";

        CreateAccountRequest request = CreateAccountRequest.builder()
                .name("Cash")
                .type(Type.CASH)
                .accountNumber("12345678")
                .initialBalance(BigDecimal.valueOf(2300))
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> accountService.create(request, email));

        verify(accountRepo, never()).save(any(Account.class));
    }

    @Test
    void getAll_shouldReturnAllAccountsOfUser() {

        String email = "vedant@test.com";

        Account cashAccount = Account.builder()
                .accountId(1L)
                .name("Wallet")
                .accountType(Type.CASH)
                .currentBalance(new BigDecimal("3000.00"))
                .user(user)
                .build();

        Account bankAccount = Account.builder()
                .accountId(2L)
                .name("HDFC Savings")
                .accountType(Type.BANK)
                .accountNumber("123456789")
                .currentBalance(new BigDecimal("20000.00"))
                .user(user)
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(accountRepo.findByUser(user))
                .thenReturn(List.of(cashAccount, bankAccount));

        List<AccountDetailsResponse> response = accountService.getAll(email);

        assertEquals(2, response.size());

        assertEquals("Wallet", response.get(0).name());
        assertEquals(Type.CASH, response.get(0).type());
        assertEquals(new BigDecimal("3000.00"),
                response.get(0).balance());

        assertEquals("HDFC Savings", response.get(1).name());
        assertEquals(Type.BANK, response.get(1).type());
        assertEquals("123456789",
                response.get(1).accountNumber());

        verify(userRepository).findByEmail(email);
        verify(accountRepo).findByUser(user);
    }

    @Test
    void getAll_shouldReturnEmptyListWhenUserHasNoAccounts() {

        String email = "vedant@test.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(accountRepo.findByUser(user))
                .thenReturn(List.of());

        List<AccountDetailsResponse> response = accountService.getAll(email);

        assertEquals(0, response.size());

        verify(accountRepo).findByUser(user);
    }

    @Test
    void getAll_shouldThrowExceptionWhenUserDoesNotExist() {

        String email = "unknown@test.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> accountService.getAll(email));

        verify(accountRepo, never()).findByUser(any(User.class));
    }
}
