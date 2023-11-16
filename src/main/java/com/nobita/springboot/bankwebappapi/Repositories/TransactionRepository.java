package com.nobita.springboot.bankwebappapi.Repositories;

import com.nobita.springboot.bankwebappapi.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}

