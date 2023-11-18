package com.nobita.springboot.bankwebappapi.Repositories;

import com.nobita.springboot.bankwebappapi.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByOrderByTransactionTimeDesc();
}

