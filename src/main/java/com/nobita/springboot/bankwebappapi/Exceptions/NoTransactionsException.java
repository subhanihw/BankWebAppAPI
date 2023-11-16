package com.nobita.springboot.bankwebappapi.Exceptions;

public class NoTransactionsException extends Exception{
    public NoTransactionsException(String msg) {
        super(msg);
    }
}
