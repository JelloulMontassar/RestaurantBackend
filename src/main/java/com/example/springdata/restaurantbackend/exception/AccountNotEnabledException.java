package com.example.springdata.restaurantbackend.exception;

public class AccountNotEnabledException extends  RuntimeException {
    private static final long serialVersionUID = 1L;

    public AccountNotEnabledException(String message) {
        super(message);
    }
}
