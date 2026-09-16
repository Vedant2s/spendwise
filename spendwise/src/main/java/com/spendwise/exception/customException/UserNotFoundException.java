package com.spendwise.exception.customException;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("User entered is not found please register");
    }
}
