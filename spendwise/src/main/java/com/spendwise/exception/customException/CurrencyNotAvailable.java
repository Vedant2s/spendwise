package com.spendwise.exception.customException;

public class CurrencyNotAvailable extends RuntimeException {
    public CurrencyNotAvailable(){
        super("Requested currency Is not found.");
    }
}
