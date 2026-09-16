package com.spendwise.exception.customException;

public class InvalidPasswordException extends RuntimeException{
    public InvalidPasswordException(){
        super("Invalid Password entered");
    }
}
