package com.nobita.springboot.bankwebappapi.Exceptions;

public class MinBalanceException extends Exception{
    public MinBalanceException() {
    }

    public MinBalanceException(String msg) {
        super(msg);
    }
}
