package com.spendwise.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spendwise.dto.CreateAccountRequest;
import com.spendwise.dto.CreateAccountResponse;
import com.spendwise.model.Account;
import com.spendwise.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepo;
    public CreateAccountResponse create(CreateAccountRequest request){
        return new CreateAccountResponse(); //add logic
    }
}
