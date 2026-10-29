package com.spendwise.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spendwise.dto.BalanceInAccountsResponse;
import com.spendwise.dto.BalanceInCurrencyResponse;
import com.spendwise.dto.BalanceOfUserResponse;
import com.spendwise.dto.BalanceTrendResponse;
import com.spendwise.dto.CashFlowResponse;
import com.spendwise.dto.SpendingStructureResponse;
import com.spendwise.exception.customException.UserNotFoundException;
import com.spendwise.model.Account;
import com.spendwise.model.Category;
import com.spendwise.model.Currency;
import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;
import com.spendwise.model.User;
import com.spendwise.repository.AccountRepository;
import com.spendwise.repository.TransactionRepository;
import com.spendwise.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DashboardService dashboardService;

    private User user;
    private Currency inr;
    private Currency usd;
    private Account savings;
    private Account wallet;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .email("test@gmail.com")
                .username("Test User")
                .build();

        inr = Currency.builder()
                .currencyId(1L)
                .currencyCode("INR")
                .symbol('₹')
                .build();

        usd = Currency.builder()
                .currencyId(2L)
                .currencyCode("USD")
                .symbol('$')
                .build();

        savings = Account.builder()
                .accountId(1L)
                .name("Savings")
                .currentBalance(new BigDecimal("50000"))
                .currency(inr)
                .user(user)
                .build();

        wallet = Account.builder()
                .accountId(2L)
                .name("Wallet")
                .currentBalance(new BigDecimal("10000"))
                .currency(inr)
                .user(user)
                .build();
    }

    /*
     * ---------------------------------------------------
     * getBalanceInCurrencies()
     * ---------------------------------------------------
     */

    @Test
    void getBalanceInCurrencies_shouldCombineAccountsWithSameCurrency() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByUser(user))
                .thenReturn(List.of(savings, wallet));

        List<BalanceInCurrencyResponse> response =
                dashboardService.getBalanceInCurrencies("test@gmail.com");

        assertEquals(1, response.size());

        BalanceInCurrencyResponse result = response.get(0);

        assertEquals("INR", result.currencyCode());
        assertEquals('₹', result.symbol());
        assertEquals(
                0,
                new BigDecimal("60000")
                        .compareTo(result.value())
        );

        verify(userRepository).findByEmail("test@gmail.com");
        verify(accountRepository).findByUser(user);
    }

    @Test
    void getBalanceInCurrencies_shouldKeepDifferentCurrenciesSeparate() {

        Account usdAccount = Account.builder()
                .accountId(3L)
                .name("USD Account")
                .currentBalance(new BigDecimal("100"))
                .currency(usd)
                .user(user)
                .build();

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByUser(user))
                .thenReturn(List.of(savings, usdAccount));

        List<BalanceInCurrencyResponse> response =
                dashboardService.getBalanceInCurrencies("test@gmail.com");

        assertEquals(2, response.size());
    }

    /*
     * ---------------------------------------------------
     * getBalanceInAccounts()
     * ---------------------------------------------------
     */

    @Test
    void getBalanceInAccounts_shouldReturnBalanceForEachAccount() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByUser(user))
                .thenReturn(List.of(savings, wallet));

        List<BalanceInAccountsResponse> response =
                dashboardService.getBalanceInAccounts("test@gmail.com");

        assertEquals(2, response.size());

        assertEquals("Savings", response.get(0).accountName());
        assertEquals("INR", response.get(0).currencyCode());

        assertEquals(
                0,
                new BigDecimal("50000")
                        .compareTo(response.get(0).balance())
        );

        assertEquals("Wallet", response.get(1).accountName());

        assertEquals(
                0,
                new BigDecimal("10000")
                        .compareTo(response.get(1).balance())
        );

        verify(accountRepository).findByUser(user);
    }

    /*
     * ---------------------------------------------------
     * getCashFlow()
     * ---------------------------------------------------
     */

    @Test
    void getCashFlow_shouldCalculateIncomeExpenseAndCashFlow() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByUser(user))
                .thenReturn(List.of(savings));

        Transaction income = Transaction.builder()
                .amount(new BigDecimal("10000"))
                .transactionType(TransactionType.INCOME)
                .build();

        Transaction expense1 = Transaction.builder()
                .amount(new BigDecimal("3000"))
                .transactionType(TransactionType.EXPENSE)
                .build();

        Transaction expense2 = Transaction.builder()
                .amount(new BigDecimal("2000"))
                .transactionType(TransactionType.EXPENSE)
                .build();

        Transaction previousIncome = Transaction.builder()
                .amount(new BigDecimal("8000"))
                .transactionType(TransactionType.INCOME)
                .build();

        Transaction previousExpense = Transaction.builder()
                .amount(new BigDecimal("3000"))
                .transactionType(TransactionType.EXPENSE)
                .build();

        when(transactionRepository.findAllByAccountInAndTimeStampBetween(
                anyList(),
                any(LocalDate.class),
                any(LocalDate.class)))
                .thenReturn(
                        List.of(income, expense1, expense2),
                        List.of(previousIncome, previousExpense)
                );

        CashFlowResponse response =
                dashboardService.getCashFlow("test@gmail.com", 30);

        assertEquals(
                0,
                new BigDecimal("10000")
                        .compareTo(response.income())
        );

        assertEquals(
                0,
                new BigDecimal("5000")
                        .compareTo(response.expense())
        );

        assertEquals(
                0,
                new BigDecimal("5000")
                        .compareTo(response.cashFlow())
        );

        // Current cashflow = 5000
        // Previous cashflow = 8000 - 3000 = 5000
        // Percentage change = 0%
        assertEquals(0.0, response.cashFlowChangePercentage());

        // Income increased from 8000 to 10000
        // (2000 / 8000) * 100 = 25%
        assertEquals(25.0, response.incomeChangePercentage());
    }

    @Test
    void getCashFlow_shouldReturnNullPercentages_whenPreviousPeriodIsZero() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByUser(user))
                .thenReturn(List.of(savings));

        Transaction income = Transaction.builder()
                .amount(new BigDecimal("10000"))
                .transactionType(TransactionType.INCOME)
                .build();

        when(transactionRepository.findAllByAccountInAndTimeStampBetween(
                anyList(),
                any(LocalDate.class),
                any(LocalDate.class)))
                .thenReturn(
                        List.of(income),
                        List.of()
                );

        CashFlowResponse response =
                dashboardService.getCashFlow("test@gmail.com", 30);

        assertNull(response.cashFlowChangePercentage());
        assertNull(response.incomeChangePercentage());
    }

    /*
     * ---------------------------------------------------
     * getSpendingStructure()
     * ---------------------------------------------------
     */

    @Test
    void getSpendingStructure_shouldCalculateCategoryPercentages() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByUser(user))
                .thenReturn(List.of(savings));

        Category food = Category.builder()
                .categoryName("Food")
                .build();

        Category travel = Category.builder()
                .categoryName("Travel")
                .build();

        Transaction foodExpense = Transaction.builder()
                .amount(new BigDecimal("600"))
                .transactionType(TransactionType.EXPENSE)
                .category(food)
                .build();

        Transaction travelExpense = Transaction.builder()
                .amount(new BigDecimal("400"))
                .transactionType(TransactionType.EXPENSE)
                .category(travel)
                .build();

        Transaction income = Transaction.builder()
                .amount(new BigDecimal("5000"))
                .transactionType(TransactionType.INCOME)
                .build();

        Transaction previousExpense = Transaction.builder()
                .amount(new BigDecimal("800"))
                .transactionType(TransactionType.EXPENSE)
                .category(food)
                .build();

        when(transactionRepository.findAllByAccountInAndTimeStampBetween(
                anyList(),
                any(LocalDate.class),
                any(LocalDate.class)))
                .thenReturn(
                        List.of(foodExpense, travelExpense, income),
                        List.of(previousExpense)
                );

        SpendingStructureResponse response =
                dashboardService.getSpendingStructure(
                        "test@gmail.com",
                        30
                );

        assertEquals(
                0,
                new BigDecimal("1000")
                        .compareTo(response.totalSpending())
        );

        assertEquals(2, response.categories().size());

        var foodResult = response.categories()
                .stream()
                .filter(c -> c.categoryName().equals("Food"))
                .findFirst()
                .orElseThrow();

        var travelResult = response.categories()
                .stream()
                .filter(c -> c.categoryName().equals("Travel"))
                .findFirst()
                .orElseThrow();

        assertEquals(
                0,
                new BigDecimal("600")
                        .compareTo(foodResult.amount())
        );

        assertEquals(60.0, foodResult.percentage());

        assertEquals(
                0,
                new BigDecimal("400")
                        .compareTo(travelResult.amount())
        );

        assertEquals(40.0, travelResult.percentage());

        // Previous = 800
        // Current = 1000
        // Increase = 200 / 800 = 25%
        assertEquals(
                25.0,
                response.changeFromPreviousPeriodPercentage()
        );
    }

    @Test
    void getSpendingStructure_shouldReturnNullChange_whenPreviousExpenseIsZero() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByUser(user))
                .thenReturn(List.of(savings));

        when(transactionRepository.findAllByAccountInAndTimeStampBetween(
                anyList(),
                any(LocalDate.class),
                any(LocalDate.class)))
                .thenReturn(
                        List.of(),
                        List.of()
                );

        SpendingStructureResponse response =
                dashboardService.getSpendingStructure(
                        "test@gmail.com",
                        30
                );

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(response.totalSpending())
        );

        assertTrue(response.categories().isEmpty());

        assertNull(
                response.changeFromPreviousPeriodPercentage()
        );
    }

    /*
     * ---------------------------------------------------
     * getActualTotalBalanceTrend()
     * ---------------------------------------------------
     */

    @Test
    void getActualTotalBalanceTrend_shouldBuildCorrectSevenDayTrend() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByUser(user))
                .thenReturn(List.of(savings));

        LocalDate today = LocalDate.now();

        Transaction income = Transaction.builder()
                .amount(new BigDecimal("10000"))
                .transactionType(TransactionType.INCOME)
                .timeStamp(today.minusDays(3))
                .build();

        Transaction expense = Transaction.builder()
                .amount(new BigDecimal("2000"))
                .transactionType(TransactionType.EXPENSE)
                .timeStamp(today.minusDays(1))
                .build();

        when(transactionRepository.findAllByAccountInAndTimeStampBetween(
                anyList(),
                any(LocalDate.class),
                any(LocalDate.class)))
                .thenReturn(List.of(income, expense));

        BalanceTrendResponse response =
                dashboardService.getActualTotalBalanceTrend(
                        "test@gmail.com",
                        "7D"
                );

        /*
         * Actual balance = 50000
         *
         * Net change =
         * +10000 - 2000
         * = +8000
         *
         * Past balance =
         * 50000 - 8000
         * = 42000
         */

        assertEquals(
                0,
                new BigDecimal("42000.00")
                        .compareTo(response.pastBalance())
        );

        assertEquals(
                0,
                new BigDecimal("50000.00")
                        .compareTo(response.actualBalance())
        );

        assertEquals(7, response.trend().size());

        // (50000 - 42000) / 42000 * 100
        // = 19.05%
        assertEquals(
                19.05,
                response.changePercentage()
        );

        // Final trend point should match actual balance.
        assertEquals(
                0,
                new BigDecimal("50000.00")
                        .compareTo(
                                response.trend()
                                        .get(response.trend().size() - 1)
                                        .balance()
                        )
        );
    }

    @Test
    void getActualTotalBalanceTrend_shouldIgnoreTransactionsWithNullAmount() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(accountRepository.findByUser(user))
                .thenReturn(List.of(savings));

        Transaction transaction = Transaction.builder()
                .amount(null)
                .transactionType(TransactionType.INCOME)
                .timeStamp(LocalDate.now())
                .build();

        when(transactionRepository.findAllByAccountInAndTimeStampBetween(
                anyList(),
                any(LocalDate.class),
                any(LocalDate.class)))
                .thenReturn(List.of(transaction));

        BalanceTrendResponse response =
                dashboardService.getActualTotalBalanceTrend(
                        "test@gmail.com",
                        "7D"
                );

        assertEquals(
                0,
                new BigDecimal("50000.00")
                        .compareTo(response.actualBalance())
        );

        assertEquals(
                0,
                new BigDecimal("50000.00")
                        .compareTo(response.pastBalance())
        );

        assertEquals(0.0d,response.changePercentage());
    }

    @Test
    void getActualTotalBalanceTrend_shouldThrowException_whenPeriodIsInvalid() {

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> dashboardService
                                .getActualTotalBalanceTrend(
                                        "test@gmail.com",
                                        "100D"
                                )
                );

        assertEquals(
                "Invalid period. Allowed values: 7D, 30D, 12W",
                exception.getMessage()
        );

        verifyNoInteractions(
                userRepository,
                accountRepository,
                transactionRepository
        );
    }

    /*
     * ---------------------------------------------------
     * getActualTotalBalance()
     * ---------------------------------------------------
     */

    @Test
    void getActualTotalBalance_shouldCalculateCurrentBalanceAndPercentageChange() {

        when(userRepository.findByEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        /*
         * getActualTotalBalance() ultimately calls
         * getAccountsOfUser() more than once because
         * getCashFlow() also calls it.
         */
        when(accountRepository.findByUser(user))
                .thenReturn(List.of(savings, wallet));

        Transaction currentIncome = Transaction.builder()
                .amount(new BigDecimal("10000"))
                .transactionType(TransactionType.INCOME)
                .build();

        Transaction currentExpense = Transaction.builder()
                .amount(new BigDecimal("5000"))
                .transactionType(TransactionType.EXPENSE)
                .build();

        Transaction previousIncome = Transaction.builder()
                .amount(new BigDecimal("5000"))
                .transactionType(TransactionType.INCOME)
                .build();

        when(transactionRepository.findAllByAccountInAndTimeStampBetween(
                anyList(),
                any(LocalDate.class),
                any(LocalDate.class)))
                .thenReturn(
                        List.of(currentIncome, currentExpense),
                        List.of(previousIncome)
                );

        BalanceOfUserResponse response =
                dashboardService.getActualTotalBalance(
                        "test@gmail.com",
                        "30D"
                );

        /*
         * Current account balance:
         * 50000 + 10000 = 60000
         *
         * Current cashflow:
         * 10000 - 5000 = 5000
         *
         * Past balance:
         * 60000 - 5000 = 55000
         *
         * Change:
         * 5000 / 55000 * 100 = 9.09%
         */

        assertEquals(
                0,
                new BigDecimal("60000")
                        .compareTo(response.currentBalance())
        );

        assertEquals(
                9.09,
                response.percentChangeInBalance()
        );
    }

    @Test
    void getActualTotalBalance_shouldThrowException_whenPeriodIsInvalid() {

        assertThrows(
                IllegalArgumentException.class,
                () -> dashboardService
                        .getActualTotalBalance(
                                "test@gmail.com",
                                "5Y"
                        )
        );

        verifyNoInteractions(
                userRepository,
                accountRepository,
                transactionRepository
        );
    }

    /*
     * ---------------------------------------------------
     * Common UserNotFound behavior
     * ---------------------------------------------------
     */

    @Test
    void getBalanceInAccounts_shouldThrowUserNotFoundException_whenUserDoesNotExist() {

        when(userRepository.findByEmail("missing@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> dashboardService
                        .getBalanceInAccounts("missing@gmail.com")
        );

        verify(userRepository)
                .findByEmail("missing@gmail.com");

        verifyNoInteractions(accountRepository);
        verifyNoInteractions(transactionRepository);
    }
}