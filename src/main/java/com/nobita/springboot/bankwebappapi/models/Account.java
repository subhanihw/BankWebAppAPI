package com.nobita.springboot.bankwebappapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nobita.springboot.bankwebappapi.Exceptions.*;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.nobita.springboot.bankwebappapi.models.Constants.TRANSACTION_CHARGE;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;
    private String accountNumber;
    private double balance;
    private String accountType;

    @Nullable
    private Double interestRate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customerId")
    private Customer customer;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public void withdraw(double amount) throws InsufficientBalanceException, MinBalanceException {
        if (balance - amount < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        if (balance - amount < 110) {
            throw new MinBalanceException("Balance must be greater than 100 after withdraw. Transaction Charge: $10");
        }
        balance -= amount;
        applyTransactionCharge(TRANSACTION_CHARGE);
    }

    public void deposit(double amount) throws RequiresPanException, MinBalanceException {
        if (amount <= 0)
            throw new MinBalanceException("Amount must be greater than 0.");
        if (amount > 5000000 && (customer.getPanNum() == null || customer.getPanNum().isEmpty())) {
            throw new RequiresPanException("Requires Pan details to deposit more than 5000000");
        }
        if (amount < 100) {
            throw new MinBalanceException("Amount must be greater than 100 to deposit");
        }
        balance += amount;
        applyTransactionCharge(TRANSACTION_CHARGE);
    }

    public void applyTransactionCharge(double charge) {
        balance -= charge;
    }

    public Transaction getMaxTransactionAmount(String type) throws NoTransactionsException {
        Optional<Transaction> maxTransaction = transactions.stream()
                .filter(transaction -> transaction.getType().equalsIgnoreCase(type))
                .max(Comparator.comparingDouble(Transaction::getAmount));
        if (maxTransaction.isPresent())
            return maxTransaction.get();
        throw new NoTransactionsException("No Deposits were made with the current Account.");
    }

    public Transaction getMinTransactionAmount(String type) throws NoTransactionsException {
        Optional<Transaction> minTransaction = transactions.stream()
                .filter(transaction -> transaction.getType().equalsIgnoreCase(type))
                .min(Comparator.comparingDouble(Transaction::getAmount));
        if (minTransaction.isPresent())
            return minTransaction.get();
        throw new NoTransactionsException("No Withdrawals were made with the current Account.");
    }
}