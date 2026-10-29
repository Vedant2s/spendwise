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

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.spendwise.dto.BalanceInAccountsResponse;
import com.spendwise.dto.BalanceInCurrencyResponse;
import com.spendwise.dto.BalanceOfUserResponse;
import com.spendwise.dto.BalanceTrendResponse;
import com.spendwise.dto.CashFlowResponse;
import com.spendwise.dto.CategorywiseSpending;
import com.spendwise.dto.SpendingStructureResponse;
import com.spendwise.service.DashboardService;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    private MockMvc mockMvc;

    private final String email = "vedant@gmail.com";

    private UsernamePasswordAuthenticationToken authentication;

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController dashboardController;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(dashboardController)
                .build();

        authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null
                );
    }

    /*
     * =====================================================
     * GET /dashboard/balance/currency
     * =====================================================
     */

    @Test
    void getCurrency_shouldReturnOkAndCurrencyBalances()
            throws Exception {

        BalanceInCurrencyResponse inr =
                new BalanceInCurrencyResponse(
                        1L,
                        "INR",
                        '₹',
                        new BigDecimal("60000")
                );

        BalanceInCurrencyResponse usd =
                new BalanceInCurrencyResponse(
                        2L,
                        "USD",
                        '$',
                        new BigDecimal("500")
                );

        when(dashboardService.getBalanceInCurrencies(email))
                .thenReturn(List.of(inr, usd));

        mockMvc.perform(
                        get("/dashboard/balance/currency")
                                .principal(authentication)
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.length()")
                        .value(2))

                .andExpect(jsonPath("$[0].currencyId")
                        .value(1))
                .andExpect(jsonPath("$[0].currencyCode")
                        .value("INR"))
                .andExpect(jsonPath("$[0].symbol")
                        .value("₹"))
                .andExpect(jsonPath("$[0].value")
                        .value(60000))

                .andExpect(jsonPath("$[1].currencyId")
                        .value(2))
                .andExpect(jsonPath("$[1].currencyCode")
                        .value("USD"))
                .andExpect(jsonPath("$[1].symbol")
                        .value("$"))
                .andExpect(jsonPath("$[1].value")
                        .value(500));

        verify(dashboardService)
                .getBalanceInCurrencies(email);
    }

    /*
     * =====================================================
     * GET /dashboard/balance/accounts
     * =====================================================
     */

    @Test
    void getBalanceInAccounts_shouldReturnOkAndAccountBalances()
            throws Exception {

        BalanceInAccountsResponse savings =
                BalanceInAccountsResponse.builder()
                        .currencyCode("INR")
                        .symbol('₹')
                        .accountId(1L)
                        .accountName("Savings")
                        .balance(new BigDecimal("50000"))
                        .build();

        BalanceInAccountsResponse wallet =
                BalanceInAccountsResponse.builder()
                        .currencyCode("INR")
                        .symbol('₹')
                        .accountId(2L)
                        .accountName("Wallet")
                        .balance(new BigDecimal("10000"))
                        .build();

        when(dashboardService.getBalanceInAccounts(email))
                .thenReturn(List.of(savings, wallet));

        mockMvc.perform(
                        get("/dashboard/balance/accounts")
                                .principal(authentication)
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.length()")
                        .value(2))

                .andExpect(jsonPath("$[0].currencyCode")
                        .value("INR"))
                .andExpect(jsonPath("$[0].symbol")
                        .value("₹"))
                .andExpect(jsonPath("$[0].accountId")
                        .value(1))
                .andExpect(jsonPath("$[0].accountName")
                        .value("Savings"))
                .andExpect(jsonPath("$[0].balance")
                        .value(50000))

                .andExpect(jsonPath("$[1].accountId")
                        .value(2))
                .andExpect(jsonPath("$[1].accountName")
                        .value("Wallet"))
                .andExpect(jsonPath("$[1].balance")
                        .value(10000));

        verify(dashboardService)
                .getBalanceInAccounts(email);
    }

    /*
     * =====================================================
     * GET /dashboard/cash-flow?period=30
     * =====================================================
     */

    @Test
    void getCashFlow_shouldReturnOkAndCashFlow()
            throws Exception {

        CashFlowResponse response =
                CashFlowResponse.builder()
                        .income(new BigDecimal("50000"))
                        .expense(new BigDecimal("30000"))
                        .cashFlow(new BigDecimal("20000"))
                        .incomeChangePercentage(25.0)
                        .cashFlowChangePercentage(10.0)
                        .build();

        when(dashboardService.getCashFlow(email, 30))
                .thenReturn(response);

        mockMvc.perform(
                        get("/dashboard/cash-flow")
                                .principal(authentication)
                                .param("period", "30")
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.income")
                        .value(50000))
                .andExpect(jsonPath("$.expense")
                        .value(30000))
                .andExpect(jsonPath("$.cashFlow")
                        .value(20000))
                .andExpect(jsonPath("$.incomeChangePercentage")
                        .value(25.0))
                .andExpect(jsonPath("$.cashFlowChangePercentage")
                        .value(10.0));

        verify(dashboardService)
                .getCashFlow(email, 30);
    }

    /*
     * =====================================================
     * GET /dashboard/spending/structure?period=30
     * =====================================================
     */

    @Test
    void getSpendingStructure_shouldReturnOkAndSpendingStructure()
            throws Exception {

        CategorywiseSpending food =
                CategorywiseSpending.builder()
                        .categoryName("Food")
                        .amount(new BigDecimal("6000"))
                        .percentage(60.0)
                        .build();

        CategorywiseSpending travel =
                CategorywiseSpending.builder()
                        .categoryName("Travel")
                        .amount(new BigDecimal("4000"))
                        .percentage(40.0)
                        .build();

        SpendingStructureResponse response =
                SpendingStructureResponse.builder()
                        .totalSpending(new BigDecimal("10000"))
                        .categories(List.of(food, travel))
                        .changeFromPreviousPeriodPercentage(25.0)
                        .build();

        when(dashboardService.getSpendingStructure(email, 30))
                .thenReturn(response);

        mockMvc.perform(
                        get("/dashboard/spending/structure")
                                .principal(authentication)
                                .param("period", "30")
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.totalSpending")
                        .value(10000))

                .andExpect(jsonPath("$.categories.length()")
                        .value(2))

                .andExpect(jsonPath("$.categories[0].categoryName")
                        .value("Food"))
                .andExpect(jsonPath("$.categories[0].amount")
                        .value(6000))
                .andExpect(jsonPath("$.categories[0].percentage")
                        .value(60.0))

                .andExpect(jsonPath("$.categories[1].categoryName")
                        .value("Travel"))
                .andExpect(jsonPath("$.categories[1].amount")
                        .value(4000))
                .andExpect(jsonPath("$.categories[1].percentage")
                        .value(40.0))

                .andExpect(jsonPath(
                        "$.changeFromPreviousPeriodPercentage")
                        .value(25.0));

        verify(dashboardService)
                .getSpendingStructure(email, 30);
    }

    /*
     * =====================================================
     * GET /dashboard/balance/trend?period=7D
     * =====================================================
     */

    @Test
    void getActualTotalBalanceTrend_shouldReturnOkAndBalanceTrend()
            throws Exception {

        BalanceTrendResponse response =
                BalanceTrendResponse.builder()
                        .actualBalance(
                                new BigDecimal("50000.00")
                        )
                        .pastBalance(
                                new BigDecimal("42000.00")
                        )
                        .changePercentage(19.05)
                        .trend(List.of())
                        .build();

        when(dashboardService
                .getActualTotalBalanceTrend(
                        email,
                        "7D"
                ))
                .thenReturn(response);

        mockMvc.perform(
                        get("/dashboard/balance/trend")
                                .principal(authentication)
                                .param("period", "7D")
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.actualBalance")
                        .value(50000.00))
                .andExpect(jsonPath("$.pastBalance")
                        .value(42000.00))
                .andExpect(jsonPath("$.changePercentage")
                        .value(19.05))
                .andExpect(jsonPath("$.trend.length()")
                        .value(0));

        verify(dashboardService)
                .getActualTotalBalanceTrend(
                        email,
                        "7D"
                );
    }

    /*
     * =====================================================
     * GET /dashboard/balance/actual-balance?period=30D
     * =====================================================
     */

    @Test
    void getActualTotalBalance_shouldReturnOkAndActualBalance()
            throws Exception {

        BalanceOfUserResponse response =
                BalanceOfUserResponse.builder()
                        .currentBalance(
                                new BigDecimal("60000")
                        )
                        .percentChangeInBalance(9.09)
                        .build();

        when(dashboardService.getActualTotalBalance(
                email,
                "30D"
        )).thenReturn(response);

        mockMvc.perform(
                        get("/dashboard/balance/actual-balance")
                                .principal(authentication)
                                .param("period", "30D")
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.currentBalance")
                        .value(60000))
                .andExpect(jsonPath("$.percentChangeInBalance")
                        .value(9.09));

        verify(dashboardService)
                .getActualTotalBalance(
                        email,
                        "30D"
                );
    }
}