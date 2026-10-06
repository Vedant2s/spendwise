package com.spendwise.exception.customException;


public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(){
        super("Account not found");
    }
}
