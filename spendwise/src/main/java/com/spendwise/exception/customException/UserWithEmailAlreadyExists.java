package com.spendwise.exception.customException;

public class UserWithEmailAlreadyExists extends RuntimeException{
    public UserWithEmailAlreadyExists(){
        super("User with the given email already exists");
    }
}
