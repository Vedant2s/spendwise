package com.spendwise.controller;

import org.springframework.web.bind.annotation.RestController;

import com.spendwise.dto.BalanceInCurrencyResponse;
import com.spendwise.service.DashboardService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    @GetMapping("balance/currency")
    public ResponseEntity<List<BalanceInCurrencyResponse>> getCurrency(Authentication authentication) {
        return ResponseEntity.ok(dashboardService.getBalanceInCurrencies(authentication.getName()));
    }

}
