package com.spendwise.service;

import org.springframework.stereotype.Service;

import com.spendwise.dto.UpdateCurPreferenceRequest;
import com.spendwise.dto.UserCurPreferenceResponse;
import com.spendwise.exception.customException.CurrencyNotAvailable;
import com.spendwise.model.Currency;
import com.spendwise.repository.CurrencyRepository;

@Service
public class UserService {
    final CurrencyRepository currencyRepos;

    public UserService(CurrencyRepository currencyRepos) {
        this.currencyRepos = currencyRepos;
    }

    public UserCurPreferenceResponse setCurrForUser(UpdateCurPreferenceRequest request) {
        Currency cur=currencyRepos.findById(request.currencyId())
        .orElseThrow(()->new CurrencyNotAvailable());
        return new UserCurPreferenceResponse(
            cur.getCurrencyId(),
            cur.getCurrencyCode(),
            cur.getSymbol()
        );
    }
}
