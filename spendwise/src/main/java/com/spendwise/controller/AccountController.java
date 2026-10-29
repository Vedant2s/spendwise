package com.spendwise.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spendwise.dto.AccountDetailsResponse;
import com.spendwise.dto.CreateAccountRequest;
import com.spendwise.dto.CreateAccountResponse;
import com.spendwise.dto.ExpenseInPeriodResponse;
import com.spendwise.dto.TransactionResponse;
import com.spendwise.service.AccountService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/accounts")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody CreateAccountRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                accountService.create(
                        request, authentication.getName()));
    }

    @GetMapping("/wallet")
    public ResponseEntity<List<AccountDetailsResponse>> getAllAccounts(
            Authentication authentication) {
        return ResponseEntity.ok(
                accountService.getAll(
                        authentication.getName()));
    }
    @GetMapping("/last-records")
    public ResponseEntity<List<TransactionResponse>> getRecordsInPeriod(
            Authentication authentication,@RequestParam("period")  String days) {
        return ResponseEntity.ok(
                accountService.getRecordsInPeriod(
                        authentication.getName(),days));
    }
    @GetMapping("/{accountId}/expense-structure")
    public ResponseEntity<List<ExpenseInPeriodResponse>> getExpenseRecordsInPeriod(
            Authentication authentication,@RequestParam("period")  String days,@PathVariable Long accountId) {
        return ResponseEntity.ok(
                accountService.getExpenseRecordsInPeriod(
                        authentication.getName(),days,accountId));
    }
}
