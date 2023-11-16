package com.nobita.springboot.bankwebappapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int transactionID;

    private String type;
    private double amount;
    private double charge;
    private double initialBalance;
    private double remainingBalance;
    private LocalDateTime transactionTime;

    @ManyToOne(cascade = CascadeType.ALL)
    private Account account;

    public Transaction(String type, double amount, double transactionCharge, double initialBalance, double balance) {
        this.type = type;
        this.amount = amount;
        this.charge = transactionCharge;
        this.initialBalance = initialBalance;
        this.remainingBalance = balance;
        this.transactionTime = LocalDateTime.now();
    }
}
