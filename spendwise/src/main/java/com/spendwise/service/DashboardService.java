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
import com.spendwise.dto.CashFlowResponse;
import com.spendwise.exception.customException.UserNotFoundException;
import com.spendwise.model.Account;
import com.spendwise.model.Currency;
import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;
import com.spendwise.repository.AccountRepository;
import com.spendwise.repository.CurrencyRepository;
import com.spendwise.repository.TransactionRepository;
import com.spendwise.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final CurrencyRepository currencyRepository;
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
                .findAllByAccountsInAndCreatedAtBetween(accounts, startDate, endDate);
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
                .findAllByAccountsInAndCreatedAtBetween(accounts, startDate.minusDays(days), startDate);
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
}
