package com.spendwise.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.spendwise.dto.BalanceInAccountsResponse;
import com.spendwise.dto.BalanceInCurrencyResponse;
import com.spendwise.exception.customException.UserNotFoundException;
import com.spendwise.model.Account;
import com.spendwise.model.Currency;
import com.spendwise.repository.AccountRepository;
import com.spendwise.repository.CurrencyRepository;
import com.spendwise.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final CurrencyRepository currencyRepository;
    private final AccountRepository accountRepository;
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
}
