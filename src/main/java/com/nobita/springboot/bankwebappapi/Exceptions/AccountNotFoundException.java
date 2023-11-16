package com.nobita.springboot.bankwebappapi.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends Exception{
    public AccountNotFoundException(String msg) {
        super(msg);
    }
}
