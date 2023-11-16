package com.nobita.springboot.bankwebappapi.Exceptions;

public class RequiresPanException extends Exception{
    public RequiresPanException(String msg) {
        super(msg);
    }
}
