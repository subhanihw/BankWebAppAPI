package com.nobita.springboot.bankwebappapi.Repositories;

import com.nobita.springboot.bankwebappapi.models.Account;
import com.nobita.springboot.bankwebappapi.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
