package com.spendwise.controller;

import org.springframework.web.bind.annotation.RestController;

import com.spendwise.dto.BalanceInAccountsResponse;
import com.spendwise.dto.BalanceInCurrencyResponse;
import com.spendwise.dto.BalanceOfUserResponse;
import com.spendwise.dto.BalanceTrendResponse;
import com.spendwise.dto.CashFlowResponse;
import com.spendwise.dto.SpendingStructureResponse;
import com.spendwise.service.DashboardService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    @GetMapping("/balance/currency")
    public ResponseEntity<List<BalanceInCurrencyResponse>> getCurrency(Authentication authentication) {
        return ResponseEntity.ok(dashboardService.getBalanceInCurrencies(authentication.getName()));
    }
    @GetMapping("/balance/accounts")
    public ResponseEntity<List<BalanceInAccountsResponse>> getBalanceInAccounts(Authentication authentication) {
        return ResponseEntity.ok(dashboardService.getBalanceInAccounts(authentication.getName()));
    }
    @GetMapping("/cash-flow?period={days}")
    public ResponseEntity<CashFlowResponse> getCashFlow(Authentication authentication,@PathVariable int days) {
        return ResponseEntity.ok(dashboardService.getCashFlow(authentication.getName(),days));
    }
    @GetMapping("/spending/structure?period={days}")
    public ResponseEntity<SpendingStructureResponse> getSpendingStructure(Authentication authentication,@PathVariable int days) {
        return ResponseEntity.ok(dashboardService.getSpendingStructure(authentication.getName(),days));
    }
    @GetMapping("/balance/trend?period={days}")
    public ResponseEntity<BalanceTrendResponse> getActualTotalBalanceTrend(Authentication authentication,@PathVariable String period) {
        return ResponseEntity.ok(dashboardService.getActualTotalBalanceTrend(authentication.getName(),period));
    }
    @GetMapping("/balance/actual-balance?period={days}")
    public ResponseEntity<BalanceOfUserResponse> getActualTotalBalance(Authentication authentication,@PathVariable String period) {
        return ResponseEntity.ok(dashboardService.getActualTotalBalance(authentication.getName(),period));
    }
}
