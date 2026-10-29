package com.spendwise.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spendwise.dto.CurrencyOptionResponse;
import com.spendwise.repository.CurrencyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepos;

    public List<CurrencyOptionResponse> getAll(){
        return currencyRepos.findAll()
        .stream().map(cur->
            new CurrencyOptionResponse(
                cur.getCurrencyCode(),
                cur.getSymbol(),
                cur.getExchangeRate()
            )
        ).toList();
    }
}
