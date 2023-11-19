package com.nobita.springboot.bankwebappapi.controllers;

import com.nobita.springboot.bankwebappapi.Exceptions.*;
import com.nobita.springboot.bankwebappapi.models.Transaction;
import com.nobita.springboot.bankwebappapi.models.dto.AddTransactionDTO;
import com.nobita.springboot.bankwebappapi.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PostMapping("/transactions/makeTransaction")
    public ResponseEntity<Transaction> makeTransaction(@RequestBody AddTransactionDTO transactionDTO)
            throws AccountNotFoundException, InvalidTransactionType, RequiresPanException,
            InsufficientBalanceException, MinBalanceException {

        Transaction transaction;
        try {
            transaction = transactionService.addTransaction(transactionDTO);
        } catch (Exception e) {
            if (e instanceof AccountNotFoundException) {
                throw (AccountNotFoundException) e;
            } else if (e instanceof InvalidTransactionType) {
                throw (InvalidTransactionType) e;
            } else if (e instanceof RequiresPanException) {
                throw (RequiresPanException) e;
            } else if (e instanceof InsufficientBalanceException) {
                throw (InsufficientBalanceException) e;
            } else if (e instanceof MinBalanceException) {
                throw (MinBalanceException) e;
            } else {
                throw new RuntimeException("Unexpected exception occurred: " + e.getMessage(), e);
            }
        }
        return ResponseEntity.ok(transaction);
    }


    @GetMapping("GetTransactionByAccountNumber")
    public ResponseEntity<List<Transaction>> getTransactionByAccountNum(@RequestParam String accountNum) throws AccountNotFoundException {
        return ResponseEntity.ok(transactionService.getTransactionByAccountNum(accountNum));
    }
}
