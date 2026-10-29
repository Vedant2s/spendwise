package com.spendwise.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.spendwise.dto.AccountDetailsResponse;
import com.spendwise.dto.CreateAccountRequest;
import com.spendwise.dto.CreateAccountResponse;
import com.spendwise.dto.ExpenseInPeriodResponse;
import com.spendwise.dto.TransactionResponse;
import com.spendwise.dto.Type;
import com.spendwise.model.TransactionType;
import com.spendwise.service.AccountService;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private UsernamePasswordAuthenticationToken authentication;

    private final String email = "vedant@gmail.com";

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(accountController)
                .build();

        authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null
                );
    }

    /*
     * =====================================================
     * POST /accounts
     * =====================================================
     */

    @Test
    void createAccount_shouldReturnCreatedAndCreatedAccount()
            throws Exception {

        // Arrange
        CreateAccountResponse response =
                CreateAccountResponse.builder()
                        .id(1L)
                        .name("Savings Account")
                        .type(Type.BANK)
                        .accountNumber("ACC123")
                        .balance(new BigDecimal("50000"))
                        .build();

        when(accountService.create(
                any(CreateAccountRequest.class),
                eq(email)))
                .thenReturn(response);

        String requestBody = """
                {
                    "name": "Savings Account",
                    "type": "BANK",
                    "accountNumber": "ACC123",
                    "initialBalance": 50000
                }
                """;

        // Act + Assert
        mockMvc.perform(
                        post("/accounts")
                                .principal(authentication)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name")
                        .value("Savings Account"))
                .andExpect(jsonPath("$.type")
                        .value("BANK"))
                .andExpect(jsonPath("$.accountNumber")
                        .value("ACC123"))
                .andExpect(jsonPath("$.balance")
                        .value(50000));

        /*
         * Verify that JSON was correctly converted into
         * CreateAccountRequest.
         */
        ArgumentCaptor<CreateAccountRequest> requestCaptor =
                ArgumentCaptor.forClass(
                        CreateAccountRequest.class
                );

        verify(accountService).create(
                requestCaptor.capture(),
                eq(email)
        );

        CreateAccountRequest capturedRequest =
                requestCaptor.getValue();

        org.junit.jupiter.api.Assertions.assertEquals(
                "Savings Account",
                capturedRequest.name()
        );

        org.junit.jupiter.api.Assertions.assertEquals(
                Type.BANK,
                capturedRequest.type()
        );

        org.junit.jupiter.api.Assertions.assertEquals(
                "ACC123",
                capturedRequest.accountNumber()
        );

        org.junit.jupiter.api.Assertions.assertEquals(
                0,
                new BigDecimal("50000")
                        .compareTo(
                                capturedRequest.initialBalance()
                        )
        );
    }

    /*
     * =====================================================
     * GET /accounts/wallet
     * =====================================================
     */

    @Test
    void getAllAccounts_shouldReturnOkAndAllAccounts()
            throws Exception {

        // Arrange
        AccountDetailsResponse savings =
                AccountDetailsResponse.builder()
                        .id(1L)
                        .name("Savings Account")
                        .type(Type.BANK)
                        .accountNumber("ACC123")
                        .balance(new BigDecimal("50000"))
                        .build();

        AccountDetailsResponse cash =
                AccountDetailsResponse.builder()
                        .id(2L)
                        .name("Cash Wallet")
                        .type(Type.CASH)
                        .accountNumber(null)
                        .balance(new BigDecimal("5000"))
                        .build();

        when(accountService.getAll(email))
                .thenReturn(List.of(savings, cash));

        // Act + Assert
        mockMvc.perform(
                        get("/accounts/wallet")
                                .principal(authentication)
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.length()")
                        .value(2))

                .andExpect(jsonPath("$[0].id")
                        .value(1))
                .andExpect(jsonPath("$[0].name")
                        .value("Savings Account"))
                .andExpect(jsonPath("$[0].type")
                        .value("BANK"))
                .andExpect(jsonPath("$[0].accountNumber")
                        .value("ACC123"))
                .andExpect(jsonPath("$[0].balance")
                        .value(50000))

                .andExpect(jsonPath("$[1].id")
                        .value(2))
                .andExpect(jsonPath("$[1].name")
                        .value("Cash Wallet"))
                .andExpect(jsonPath("$[1].type")
                        .value("CASH"))
                .andExpect(jsonPath("$[1].balance")
                        .value(5000));

        verify(accountService)
                .getAll(email);
    }

    /*
     * =====================================================
     * GET /accounts/last-records?period=30D
     * =====================================================
     */

    @Test
    void getRecordsInPeriod_shouldReturnOkAndTransactions()
            throws Exception {

        // Arrange
        TransactionResponse foodExpense =
                TransactionResponse.builder()
                        .category("Food")
                        .amount(new BigDecimal("850"))
                        .currCode("INR")
                        .date(LocalDate.of(2026, 10, 20))
                        .type(TransactionType.EXPENSE)
                        .build();

        TransactionResponse salary =
                TransactionResponse.builder()
                        .category("Salary")
                        .amount(new BigDecimal("50000"))
                        .currCode("INR")
                        .date(LocalDate.of(2026, 10, 1))
                        .type(TransactionType.INCOME)
                        .build();

        when(accountService.getRecordsInPeriod(
                email,
                "30D"))
                .thenReturn(
                        List.of(foodExpense, salary)
                );

        // Act + Assert
        mockMvc.perform(
                        get("/accounts/last-records")
                                .principal(authentication)
                                .param("period", "30D")
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.length()")
                        .value(2))

                .andExpect(jsonPath("$[0].category")
                        .value("Food"))
                .andExpect(jsonPath("$[0].amount")
                        .value(850))
                .andExpect(jsonPath("$[0].currCode")
                        .value("INR"))
                .andExpect(jsonPath("$[0].date")
                        .value("2026-10-20"))
                .andExpect(jsonPath("$[0].type")
                        .value("EXPENSE"))

                .andExpect(jsonPath("$[1].category")
                        .value("Salary"))
                .andExpect(jsonPath("$[1].amount")
                        .value(50000))
                .andExpect(jsonPath("$[1].currCode")
                        .value("INR"))
                .andExpect(jsonPath("$[1].date")
                        .value("2026-10-01"))
                .andExpect(jsonPath("$[1].type")
                        .value("INCOME"));

        verify(accountService)
                .getRecordsInPeriod(
                        email,
                        "30D"
                );
    }

    /*
     * =====================================================
     * GET /accounts/{accountId}/expense-structure
     * =====================================================
     */

    @Test
    void getExpenseRecordsInPeriod_shouldReturnOkAndExpenseStructure()
            throws Exception {

        // Arrange
        Long accountId = 1L;

        ExpenseInPeriodResponse food =
                ExpenseInPeriodResponse.builder()
                        .categoryName("Food")
                        .amount(new BigDecimal("6000"))
                        .currCode("INR")
                        .percentage(60.0)
                        .build();

        ExpenseInPeriodResponse travel =
                ExpenseInPeriodResponse.builder()
                        .categoryName("Travel")
                        .amount(new BigDecimal("4000"))
                        .currCode("INR")
                        .percentage(40.0)
                        .build();

        when(accountService
                .getExpenseRecordsInPeriod(
                        email,
                        "30D",
                        accountId
                ))
                .thenReturn(
                        List.of(food, travel)
                );

        // Act + Assert
        mockMvc.perform(
                        get(
                                "/accounts/{accountId}/expense-structure",
                                accountId
                        )
                        .principal(authentication)
                        .param("period", "30D")
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.length()")
                        .value(2))

                .andExpect(jsonPath("$[0].categoryName")
                        .value("Food"))
                .andExpect(jsonPath("$[0].amount")
                        .value(6000))
                .andExpect(jsonPath("$[0].currCode")
                        .value("INR"))
                .andExpect(jsonPath("$[0].percentage")
                        .value(60.0))

                .andExpect(jsonPath("$[1].categoryName")
                        .value("Travel"))
                .andExpect(jsonPath("$[1].amount")
                        .value(4000))
                .andExpect(jsonPath("$[1].currCode")
                        .value("INR"))
                .andExpect(jsonPath("$[1].percentage")
                        .value(40.0));

        verify(accountService)
                .getExpenseRecordsInPeriod(
                        email,
                        "30D",
                        accountId
                );
    }

    /*
     * =====================================================
     * Empty response cases
     * =====================================================
     */

    @Test
    void getAllAccounts_shouldReturnEmptyArray_whenUserHasNoAccounts()
            throws Exception {

        when(accountService.getAll(email))
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/accounts/wallet")
                                .principal(authentication)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()")
                        .value(0));

        verify(accountService)
                .getAll(email);
    }

    @Test
    void getRecordsInPeriod_shouldReturnEmptyArray_whenNoTransactionsExist()
            throws Exception {

        when(accountService.getRecordsInPeriod(
                email,
                "7D"))
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/accounts/last-records")
                                .principal(authentication)
                                .param("period", "7D")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()")
                        .value(0));

        verify(accountService)
                .getRecordsInPeriod(
                        email,
                        "7D"
                );
    }
}