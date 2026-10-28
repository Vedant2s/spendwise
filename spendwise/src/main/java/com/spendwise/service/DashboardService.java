package com.spendwise.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.spendwise.dto.BalanceInAccountsResponse;
import com.spendwise.dto.BalanceInCurrencyResponse;
import com.spendwise.dto.BalanceOfUserResponse;
import com.spendwise.dto.BalanceTrendPoint;
import com.spendwise.dto.BalanceTrendResponse;
import com.spendwise.dto.CashFlowResponse;
import com.spendwise.dto.CategorywiseSpending;
import com.spendwise.dto.SpendingStructureResponse;
import com.spendwise.exception.customException.UserNotFoundException;
import com.spendwise.model.Account;
import com.spendwise.model.Currency;
import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;
import com.spendwise.repository.AccountRepository;
// import com.spendwise.repository.CurrencyRepository;
import com.spendwise.repository.TransactionRepository;
import com.spendwise.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
//     private final CurrencyRepository currencyRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private List<Account> getAccountsOfUser(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return accountRepository.findByUser(user);
    }

    public List<BalanceInCurrencyResponse> getBalanceInCurrencies(String email) {
        List<Account> accounts = getAccountsOfUser(email);
        Map<Currency, BigDecimal> currencyTotals = new HashMap<>();
        for (Account account : accounts) {
            Currency currency = account.getCurrency();
            BigDecimal currentTotal = currencyTotals.getOrDefault(currency, BigDecimal.ZERO);
            currencyTotals.put(
                    currency,
                    currentTotal.add(account.getCurrentBalance()));
        }
        List<BalanceInCurrencyResponse> response = new ArrayList<>();
        for (Map.Entry<Currency, BigDecimal> entry : currencyTotals.entrySet()) {
            Currency currency = entry.getKey();
            response.add(
                    new BalanceInCurrencyResponse(
                            currency.getCurrencyId(),
                            currency.getCurrencyCode(),
                            currency.getSymbol(),
                            entry.getValue()));
        }
        return response;
    }

    public List<BalanceInAccountsResponse> getBalanceInAccounts(String email) {
        List<Account> accounts = getAccountsOfUser(email);
        List<BalanceInAccountsResponse> response = new ArrayList<>();
        for (Account account : accounts) {
            Currency currency = account.getCurrency();
            response.add(
                    new BalanceInAccountsResponse(
                            currency.getCurrencyCode(),
                            currency.getSymbol(),
                            account.getAccountId(),
                            account.getName(),
                            account.getCurrentBalance()));
        }
        return response;
    }

    public CashFlowResponse getCashFlow(String email, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        List<Account> accounts = getAccountsOfUser(email);
        List<Transaction> transactions = transactionRepository
                .findAllByAccountInAndTimeStampBetween(accounts, startDate, endDate);
        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;
        for (Transaction transac : transactions) {
            if (transac.getTransactionType() == TransactionType.EXPENSE)
                expense = expense.add(transac.getAmount());
            else if (transac.getTransactionType() == TransactionType.INCOME)
                income = income.add(transac.getAmount());
        }
        BigDecimal cashFlow = income.subtract(expense);
        BigDecimal pastIncome = BigDecimal.ZERO;
        BigDecimal pastExpense = BigDecimal.ZERO;
        transactions = transactionRepository
                .findAllByAccountInAndTimeStampBetween(accounts, startDate.minusDays(days), startDate);
        for (Transaction transac : transactions) {
            if (transac.getTransactionType() == TransactionType.EXPENSE)
                pastExpense = pastExpense.add(transac.getAmount());
            else if (transac.getTransactionType() == TransactionType.INCOME)
                pastIncome = pastIncome.add(transac.getAmount());
        }
        BigDecimal pastCashFlow = pastIncome.subtract(pastExpense);
        Double cashFlowChangePercentage = null;
        Double incomeChangePercentage = null;

        if (pastCashFlow.compareTo(BigDecimal.ZERO) != 0) {
            cashFlowChangePercentage = cashFlow
                    .subtract(pastCashFlow)
                    .divide(pastCashFlow, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        if (pastIncome.compareTo(BigDecimal.ZERO) != 0) {
            incomeChangePercentage = income
                    .subtract(pastIncome)
                    .divide(pastIncome, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        return CashFlowResponse.builder()
                .expense(expense)
                .income(income)
                .cashFlow(cashFlow)
                .cashFlowChangePercentage(cashFlowChangePercentage)
                .incomeChangePercentage(incomeChangePercentage)
                .build();
    }

    public SpendingStructureResponse getSpendingStructure(String email, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        List<Account> accounts = getAccountsOfUser(email);
        List<Transaction> transactions = transactionRepository.findAllByAccountInAndTimeStampBetween(
                accounts,
                startDate,
                endDate);
        BigDecimal totalExpense = BigDecimal.ZERO;
        Map<String, BigDecimal> categoryTotals = new HashMap<>();
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType() == TransactionType.EXPENSE) {
                BigDecimal amount = transaction.getAmount();
                String categoryName = transaction.getCategory().getCategoryName();
                totalExpense = totalExpense.add(amount);
                categoryTotals.put(
                        categoryName,
                        categoryTotals
                                .getOrDefault(categoryName, BigDecimal.ZERO)
                                .add(amount));
            }
        }
        List<CategorywiseSpending> categories = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : categoryTotals.entrySet()) {
            double percentage = 0.0;
            if (totalExpense.compareTo(BigDecimal.ZERO) != 0) {
                percentage = entry.getValue()
                        .divide(totalExpense, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP)
                        .doubleValue();
            }
            categories.add(
                    CategorywiseSpending.builder()
                            .categoryName(entry.getKey())
                            .amount(entry.getValue())
                            .percentage(percentage)
                            .build());
        }
        LocalDate previousStartDate = startDate.minusDays(days);
        List<Transaction> previousTransactions = transactionRepository.findAllByAccountInAndTimeStampBetween(
                accounts,
                previousStartDate,
                startDate);
        BigDecimal pastExpense = BigDecimal.ZERO;
        for (Transaction transaction : previousTransactions) {
            if (transaction.getTransactionType() == TransactionType.EXPENSE) {
                pastExpense = pastExpense.add(transaction.getAmount());
            }
        }
        Double expenseChangePercentage = null;
        if (pastExpense.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal expenseDifference = totalExpense.subtract(pastExpense);
            expenseChangePercentage = expenseDifference
                    .divide(pastExpense, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        return SpendingStructureResponse.builder()
                .totalSpending(totalExpense)
                .categories(categories)
                .changeFromPreviousPeriodPercentage(expenseChangePercentage)
                .build();
    }

    public BalanceTrendResponse getActualTotalBalanceTrend(
            String email,
            String period) {
        /*
         * Step 1: Convert the selected period into days.
         */
        int days = switch (period.toUpperCase()) {
            case "7D" -> 7;
            case "30D" -> 30;
            case "12W" -> 84;

            default -> throw new IllegalArgumentException(
                    "Invalid period. Allowed values: 7D, 30D, 12W");
        };

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1L);
        /*
         * Step 2: Get all accounts belonging to the user.
         */
        List<Account> accounts = getAccountsOfUser(email);
        /*
         * Step 3: Calculate the current total balance.
         */
        BigDecimal actualBalance = BigDecimal.ZERO;
        for (Account account : accounts) {
            if (account.getCurrentBalance() != null) {
                actualBalance = actualBalance.add(
                        account.getCurrentBalance());
            }
        }
        /*
         * Step 4: Retrieve transactions for the selected period.
         *
         * The repository returns them in ascending order,
         * oldest transaction first.
         */
        List<Transaction> transactions = transactionRepository
                .findAllByAccountInAndTimeStampBetween(
                        accounts,
                        startDate,
                        endDate);
        /*
         * Step 5: Calculate the net balance change for every date.
         *
         * INCOME -> positive amount
         * EXPENSE -> negative amount
         */
        Map<LocalDate, BigDecimal> dailyNetChanges = new HashMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getAmount() == null ||
                    transaction.getTimeStamp() == null) {
                continue;
            }

            LocalDate transactionDate = transaction
                    .getTimeStamp();

            BigDecimal signedAmount;

            if (transaction.getTransactionType() == TransactionType.INCOME) {
                signedAmount = transaction.getAmount();

            } else if (transaction.getTransactionType() == TransactionType.EXPENSE) {
                signedAmount = transaction
                        .getAmount()
                        .negate();

            } else {
                /*
                 * Ignore transaction types that do not directly
                 * change the user's overall balance.
                 */
                continue;
            }

            dailyNetChanges.put(
                    transactionDate,
                    dailyNetChanges.getOrDefault(transactionDate, BigDecimal.ZERO).add(signedAmount));
        }

        /*
         * Step 6: Find the total net change during the period.
         */
        BigDecimal totalNetChange = BigDecimal.ZERO;

        for (BigDecimal dailyChange : dailyNetChanges.values()) {
            totalNetChange = totalNetChange.add(dailyChange);
        }

        /*
         * actualBalance = pastBalance + totalNetChange
         *
         * Therefore:
         *
         * pastBalance = actualBalance - totalNetChange
         */
        BigDecimal pastBalance = actualBalance.subtract(
                totalNetChange);

        /*
         * Step 7: Build one graph point for every date.
         */
        List<BalanceTrendPoint> trend = new ArrayList<>();

        BigDecimal runningBalance = pastBalance;

        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {

            BigDecimal changeForCurrentDate = dailyNetChanges.getOrDefault(
                    currentDate,
                    BigDecimal.ZERO);

            runningBalance = runningBalance.add(
                    changeForCurrentDate);

            trend.add(
                    BalanceTrendPoint.builder()
                            .date(currentDate)
                            .balance(
                                    runningBalance.setScale(
                                            2,
                                            RoundingMode.HALF_UP))
                            .build());

            currentDate = currentDate.plusDays(1);
        }

        /*
         * Step 8: Calculate percentage change.
         */
        Double balanceChangePercentage;

        if (pastBalance.compareTo(BigDecimal.ZERO) == 0) {

            /*
             * Percentage change from zero is undefined.
             *
             * Returning null allows the frontend to display
             * something such as "N/A".
             */
            balanceChangePercentage = null;

        } else {

            balanceChangePercentage = actualBalance
                    .subtract(pastBalance)
                    .divide(
                            pastBalance.abs(),
                            6,
                            RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        /*
         * Step 9: Return the complete response.
         */
        return BalanceTrendResponse.builder()
                .pastBalance(
                        pastBalance.setScale(
                                2,
                                RoundingMode.HALF_UP))
                .actualBalance(
                        actualBalance.setScale(
                                2,
                                RoundingMode.HALF_UP))
                .changePercentage(balanceChangePercentage)
                .trend(trend)
                .build();
    }

    public BalanceOfUserResponse getActualTotalBalance(String email, String period) {
        int days = switch (period.toUpperCase()) {
            case "7D" -> 7;
            case "30D" -> 30;
            case "12W" -> 84;

            default -> throw new IllegalArgumentException(
                    "Invalid period. Allowed values: 7D, 30D, 12W");
        };
        CashFlowResponse cashFlowResponse = getCashFlow(email, days);
        List<Account> accounts = getAccountsOfUser(email);
        BigDecimal actualBalance = BigDecimal.ZERO;
        for (Account account : accounts) {
            if (account.getCurrentBalance() != null) {
                actualBalance = actualBalance.add(
                        account.getCurrentBalance());
            }
        }
        BigDecimal pastBalance = actualBalance.subtract(cashFlowResponse.cashFlow());
        Double percentChangeInBalance = 0.0;
        if (pastBalance.compareTo(BigDecimal.ZERO) != 0) {

            percentChangeInBalance = actualBalance
                    .subtract(pastBalance)
                    .divide(
                            pastBalance.abs(),
                            6,
                            RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        return BalanceOfUserResponse.builder()
                .currentBalance(actualBalance)
                .percentChangeInBalance(percentChangeInBalance)
                .build();

    }
}
