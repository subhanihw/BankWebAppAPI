package com.nobita.springboot.bankwebappapi.controllers;

import com.nobita.springboot.bankwebappapi.Exceptions.AccountNotFoundException;
import com.nobita.springboot.bankwebappapi.Exceptions.AccountTypeAlreadyExistsException;
import com.nobita.springboot.bankwebappapi.Exceptions.CustomerNotFoundException;
import com.nobita.springboot.bankwebappapi.Exceptions.InvalidAccountTypeException;
import com.nobita.springboot.bankwebappapi.models.Account;
import com.nobita.springboot.bankwebappapi.models.Customer;
import com.nobita.springboot.bankwebappapi.models.dto.CreateAccountDTO;
import com.nobita.springboot.bankwebappapi.services.AccountService;
import com.nobita.springboot.bankwebappapi.services.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class AccountController {
    private AccountService accountService;
    private CustomerService customerService;
    private ModelMapper modelMapper;

    public AccountController(AccountService accountService, ModelMapper modelMapper, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PostMapping("/accounts/createAccount")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountDTO accountDTO) throws CustomerNotFoundException, InvalidAccountTypeException, AccountTypeAlreadyExistsException
    {
        Optional<Customer> customer = customerService.findByCustomerId(accountDTO.getCustomerId());

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException(String.format("Customer with id = %d is not found", accountDTO.getCustomerId()));
        }

        List<Account> accountList = accountService.getAllAccountsByCustomerId(customer.get());
        String accountType = accountDTO.getAccountType();
        if (accountService.isExistsAccountType(accountList, accountType)) {
            throw new AccountTypeAlreadyExistsException(String.format("%s account already exists for given customer", accountType));
        }
        else {
            Account account = modelMapper.map(accountDTO, Account.class);
            account.setCustomer(customer.get());
            return ResponseEntity.ok(accountService.addAccount(account));
        }
    }

    @PutMapping("/accounts/updateBalance/{accountNum}")
    public ResponseEntity<Account> updateBalance
            (@PathVariable String accountNum, @RequestParam double balance) throws AccountNotFoundException
    {
        Optional<Account> account = accountService.getAccountByAccountNumber(accountNum);

        if (account.isEmpty()) {
            throw new AccountNotFoundException(String.format("Account with Number = %s is not found", accountNum));
        }
        else {
            Account account1 = account.get();
            accountService.updateBalance(account1, balance);
            return ResponseEntity.ok(account1);
        }
    }

    @DeleteMapping("/accounts/deleteAccount/{accountNum}")
    public ResponseEntity<Account> deleteAccount
            (@PathVariable String accountNum) throws AccountNotFoundException
    {
        Optional<Account> account = accountService.getAccountByAccountNumber(accountNum);

        if (account.isEmpty()) {
            throw new AccountNotFoundException(String.format("Account with Number = %s is not found", accountNum));
        }
        else {
            accountService.deleteAccount(account.get());
            return ResponseEntity.ok(account.get());
        }
    }
}
