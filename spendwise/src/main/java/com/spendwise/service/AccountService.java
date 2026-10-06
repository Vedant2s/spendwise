package com.spendwise.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spendwise.dto.AccountDetailsResponse;
import com.spendwise.dto.CreateAccountRequest;
import com.spendwise.dto.CreateAccountResponse;
import com.spendwise.dto.Type;
import com.spendwise.model.Account;
import com.spendwise.repository.AccountRepository;
import com.spendwise.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
        private final AccountRepository accountRepo;
        private final UserRepository userRepository;

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
}
