package com.spendwise.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spendwise.dto.AccountDetailsResponse;
import com.spendwise.dto.CreateAccountRequest;
import com.spendwise.dto.CreateAccountResponse;
import com.spendwise.dto.ExpenseInPeriodResponse;
import com.spendwise.dto.TransactionResponse;
import com.spendwise.dto.Type;
import com.spendwise.model.Account;
import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;
import com.spendwise.repository.AccountRepository;
import com.spendwise.repository.TransactionRepository;
import com.spendwise.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
        private final AccountRepository accountRepo;
        private final UserRepository userRepository;
        private final TransactionRepository transactionRepository;

        public CreateAccountResponse create(CreateAccountRequest request, String email) {
                var user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                if (request.type() != Type.CASH &&
                                (request.accountNumber() == null ||
                                                request.accountNumber().isBlank())) {

                        throw new IllegalArgumentException(
                                        "Account number is required for bank accounts");
                }
                String accountNumber = request.accountNumber();
                if (accountNumber == null || accountNumber.isBlank())
                        accountNumber = null;
                var newAccount = Account.builder()
                                .accountType(request.type())
                                .name(request.name())
                                .user(user)
                                .currentBalance(request.initialBalance())
                                .accountNumber(accountNumber)
                                .build();
                accountRepo.save(newAccount);
                return CreateAccountResponse.builder()
                                .balance(newAccount.getCurrentBalance())
                                .id(newAccount.getAccountId())
                                .name(newAccount.getName())
                                .type(newAccount.getAccountType())
                                .accountNumber(accountNumber)
                                .build();
        }

        public List<AccountDetailsResponse> getAll(String email) {
                var user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                var accounts = accountRepo.findByUser(user);
                return accounts.stream()
                                .map(account -> AccountDetailsResponse.builder()
                                                .accountNumber(account.getAccountNumber())
                                                .balance(account.getCurrentBalance())
                                                .id(account.getAccountId())
                                                .name(account.getName())
                                                .type(account.getAccountType())
                                                .build())
                                .toList();
        }

        public List<TransactionResponse> getRecordsInPeriod(String email, String period) {
                var user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                var accounts = accountRepo.findByUser(user);
                int days = switch (period.toUpperCase()) {
                        case "7D" -> 7;
                        case "30D" -> 30;
                        case "12W" -> 84;

                        default -> throw new IllegalArgumentException(
                                        "Invalid period. Allowed values: 7D, 30D, 12W");
                };
                LocalDate endDate = LocalDate.now();
                LocalDate startDate = endDate.minusDays(days - 1L);
                var transactions = transactionRepository.findAllByAccountInAndTimeStampBetweenOrderByTimeStampDesc(
                                accounts, startDate, endDate);
                List<TransactionResponse> transRespList = new ArrayList<>();
                for (Transaction trans : transactions) {
                        transRespList.add(
                                        TransactionResponse.builder()
                                                        .amount(trans.getAmount())
                                                        .category(trans.getCategory().getCategoryName())
                                                        .date(trans.getTimeStamp())
                                                        .currCode(trans.getAccount().getCurrency().getCurrencyCode())
                                                        .type(trans.getTransactionType())
                                                        .build());
                }
                return transRespList;
        }

        public List<ExpenseInPeriodResponse> getExpenseRecordsInPeriod(String email, String period, Long accountId) {
                var user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                Account account = accountRepo
                                .findByAccountIdAndUser(accountId, user)
                                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
                int days = switch (period.toUpperCase()) {
                        case "7D" -> 7;
                        case "30D" -> 30;
                        case "12W" -> 84;

                        default -> throw new IllegalArgumentException(
                                        "Invalid period. Allowed values: 7D, 30D, 12W");
                };
                LocalDate endDate = LocalDate.now();
                LocalDate startDate = endDate.minusDays(days - 1L);
                var transactions = transactionRepository.findExpensesByCategory(account, TransactionType.EXPENSE,
                                startDate, endDate);
                List<ExpenseInPeriodResponse> expenseInPeriodResponses = new ArrayList<>();
                BigDecimal totalExpense = BigDecimal.ZERO;
                for (Transaction trans : transactions) {
                        totalExpense = totalExpense.add(trans.getAmount());
                }
                for (Transaction trans : transactions) {
                        expenseInPeriodResponses.add(
                                        ExpenseInPeriodResponse.builder()
                                                        .amount(trans.getAmount())
                                                        .categoryName(trans.getCategory().getCategoryName())
                                                        .currCode(trans.getAccount().getCurrency().getCurrencyCode())
                                                        .percentage(
                                                                        trans.getAmount().divide(totalExpense)
                                                                                        .setScale(2, RoundingMode.HALF_UP)
                                                                                        .doubleValue())
                                                        .build());
                }
                return expenseInPeriodResponses;
        }
}
