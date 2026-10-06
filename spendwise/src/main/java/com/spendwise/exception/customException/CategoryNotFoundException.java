package com.spendwise.exception.customException;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(){
        super("Category Does not exists");
    }
}
