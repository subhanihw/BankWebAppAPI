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
    public ResponseEntity<Transaction> makeTransaction(@RequestBody AddTransactionDTO transactionDTO){
        Transaction transaction;
        try {
            transaction = transactionService.addTransaction(transactionDTO);
        } catch (AccountNotFoundException | InvalidTransactionType | RequiresPanException |
                 InsufficientBalanceException | MinBalanceException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("GetTransactionByAccountNumber")
    public ResponseEntity<List<Transaction>> getTransactionByAccountNum(@RequestParam String accountNum) throws AccountNotFoundException {
        return ResponseEntity.ok(transactionService.getTransactionByAccountNum(accountNum));
    }
}
