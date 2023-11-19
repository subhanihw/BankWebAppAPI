package com.nobita.springboot.bankwebappapi.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidTransactionType extends Exception {
    public InvalidTransactionType() {
    }

    public InvalidTransactionType(String s) {
        super(s);
    }
}
