package com.nobita.springboot.bankwebappapi.Repositories;

import com.nobita.springboot.bankwebappapi.models.Account;
import com.nobita.springboot.bankwebappapi.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findAccountByAccountNumber(String accountNum);

    int countByAccountType(String accountType);

    List<Account> getAccountsByCustomer(Customer customer);
}
