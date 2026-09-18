package com.spendwise.controller;

import org.springframework.web.bind.annotation.RestController;

import com.spendwise.dto.CurrencyOptionResponse;
import com.spendwise.service.CurrencyService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class CurrencyController {
    private final CurrencyService currService;
    public CurrencyController(CurrencyService currService) {
        this.currService = currService;
    }
    @GetMapping("/currency")
    public List<CurrencyOptionResponse> getCurrencyOptionList(@RequestParam String param) {
        return currService.getAll() ;
    }

}
