package com.nobita.springboot.bankwebappapi.services;

import com.nobita.springboot.bankwebappapi.Exceptions.*;
import com.nobita.springboot.bankwebappapi.Repositories.TransactionRepository;
import com.nobita.springboot.bankwebappapi.models.Account;
import com.nobita.springboot.bankwebappapi.models.Transaction;
import com.nobita.springboot.bankwebappapi.models.dto.AddTransactionDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.nobita.springboot.bankwebappapi.models.Constants.TRANSACTION_CHARGE;

@Service
public class TransactionService {
    private TransactionRepository repository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository repository, AccountService service) {
        this.repository = repository;
        this.accountService = service;
    }

    public List<Transaction> getAllTransactions() {
        return repository.findAllByOrderByTransactionTimeDesc();
    }

    public Transaction addTransaction(AddTransactionDTO transactionDTO) throws AccountNotFoundException, RequiresPanException, MinBalanceException, InsufficientBalanceException, InvalidTransactionType {
        Optional<Account> account = accountService.getAccountByAccountNumber(transactionDTO.getAccountNum());

        if (account.isEmpty()) {
            throw new AccountNotFoundException();
        }

        String transactionType = transactionDTO.getType();
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionType);
        transaction.setTransactionTime(LocalDateTime.now());
        transaction.setCharge(TRANSACTION_CHARGE);
        transaction.setInitialBalance(account.get().getBalance());

        transaction.setAccount(account.get());

        if (transactionType.equalsIgnoreCase("Deposit")) {
            account.get().deposit(transaction.getAmount());
        }else if (transactionType.equalsIgnoreCase("Withdraw")) {
            account.get().withdraw(transaction.getAmount());
        }else
            throw new InvalidTransactionType("Invalid Transaction Type");
        transaction.setRemainingBalance(account.get().getBalance());

        account.get().getTransactions().add(transaction);
        return repository.save(transaction);

    }

    public List<Transaction> getTransactionByAccountNum(String accountNum) throws AccountNotFoundException {
        Optional<Account> account = accountService.getAccountByAccountNumber(accountNum);

        if (account.isEmpty()) {
            throw new AccountNotFoundException();
        }
        List<Transaction> transactions = account.get().getTransactions();
        transactions.sort(Comparator.comparing(Transaction::getTransactionTime).reversed());
        return transactions;
    }
}
